package com.ajkerdeal.app.ajkerdealadmin.ui.home

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.broadcast.ConnectivityReceiver
import com.ajkerdeal.app.ajkerdealadmin.databinding.ActivityHomeBinding
import com.ajkerdeal.app.ajkerdealadmin.fcm.FCMData
import com.ajkerdeal.app.ajkerdealadmin.ui.login.LoginActivity
import com.ajkerdeal.app.ajkerdealadmin.services.LocationUpdatesService
import com.ajkerdeal.app.ajkerdealadmin.ui.location.LocationUsesBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatConfigure
import com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet.ComplainBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.track_order.TrackOrderBottomSheet

class HomeActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var doubleBackToExitPressedOnce = false

    private val permissionRequestCode = 8620
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private var foregroundService: LocationUpdatesService? = null
    private lateinit var receiver: MyReceiver
    private lateinit var connectivityReceiver : ConnectivityReceiver
    private var currentLocation: Location? = null
    private var isLocationServiceRunning: Boolean = false

    private lateinit var appUpdateManager: AppUpdateManager
    private val requestCodeAppUpdate = 21720

    private var mBound: Boolean = false
    private var snackBar: Snackbar? = null
    private var isGPS: Boolean = false
    private lateinit var gpsUtils: GpsUtils

    private val viewModel: HomeActivityViewModel by inject()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var navigationMenuId: Int = 0
    private var menuItem: MenuItem? = null

    private val adminUserList = listOf(1, 442, 83, 240, 783)
    private val adminUserListOthers = listOf( 256, 11, 652, 132)

    private lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var geoCoder: Geocoder
    private var lastAddress: String = "Searching location"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)

        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_dashboard), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)
        //binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        manageNavMenu()
        drawerListener()
        navHeaderData()

        geoCoder = Geocoder(this, Locale.US)

        receiver = MyReceiver()
        connectivityReceiver = ConnectivityReceiver()
        gpsUtils = GpsUtils(this)
        //turnOnGPS()
        appUpdateManager()
        checkRemoteConfig()
        onNewIntent(intent)
        fcmManage()
        showLocationConsent()
    }
    fun fetchCurrentLocation() {
        if (isLocationPermission()) {
            turnOnGPS()
            val intent = Intent(this, LocationUpdatesService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Timber.tag("LocationLog").d("fetchCurrentLocation")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        Timber.d("BundleLog ${intent.extras?.bundleToString()}")
        val model: FCMData? = intent.getParcelableExtra("data")
        if (model != null) {
            checkIfShouldGoToLeaveFragment(model)
            intent.removeExtra("data")
        } else {
            val bundleExt = intent.extras
            if (bundleExt != null) {
                val notificationType = bundleExt.getString("notificationType")
                if (!notificationType.isNullOrEmpty()) {
                    val fcmModel: FCMData = FCMData(
                        bundleExt.getString("notificationType"),
                        bundleExt.getString("title"),
                        bundleExt.getString("body"),
                        bundleExt.getString("imageUrl"),
                        bundleExt.getString("bigText"),
                    )
                    Timber.d("BundleLog FCMData $fcmModel")
                    checkIfShouldGoToLeaveFragment(fcmModel)
                }
            }
            intent.removeExtra("notificationType")
        }
    }

    private fun checkIfShouldGoToLeaveFragment(fcmModel: FCMData) {
        when (fcmModel.notificationType) {
            "Leave" -> {
                navController.navigate(R.id.nav_leave)
            }
            "attendance" -> {
                navController.navigate(R.id.nav_attendance_report)
            }
            else -> {
                goToNotificationPreview(fcmModel)
            }
        }
    }

    private fun goToNotificationPreview(model: FCMData) {
        val bundle = bundleOf(
            "data" to model
        )
        navController.navigate(R.id.nav_notification, bundle)
    }

    private fun fcmManage() {
        FirebaseMessaging.getInstance().subscribeToTopic("adAdminTopic")
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            navController.currentDestination?.id != navController.graph.startDestination -> {
                super.onBackPressed()
            }
            else -> {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000L)
            }
        }
    }

    private fun manageNavMenu() {
        val menu = binding.navigationView.menu
        when (SessionManager.adminType) {
            //Fulfillment
            7 -> {
               // if(!((SessionManager.dtUserId != 256) xor  (SessionManager.dtUserId != 652))){
                     menu.clear()
                //}
                binding.navigationView.inflateMenu(R.menu.menu_drawer_operation)
            }
            /*else -> {
                val menuLogOut = menu.findItem(R.id.nav_logout)
                menuLogOut.isVisible = BuildConfig.DEBUG
            }*/
        }

        val menuDashboard = menu.findItem(R.id.nav_dashboard)
        menuDashboard.onNavDestinationSelected(navController)
    }

    private fun drawerListener() {
        if (adminUserList.contains(SessionManager.dtUserId)){
            binding.navigationView.menu.findItem(R.id.nav_chat_sr).isVisible = true
            binding.navigationView.menu.findItem(R.id.nav_chat_rider).isVisible = true
            binding.navigationView.menu.findItem(R.id.nav_chat_operation).isVisible = true
        }
        if (adminUserListOthers.contains(SessionManager.dtUserId)){
            binding.navigationView.menu.findItem(R.id.nav_chat_operation).isVisible = true
            // binding.navigationView.menu.getItem(R.id.nav_chat_operation).isVisible = true
        }
        binding.navigationView.setNavigationItemSelectedListener { item ->
            navigationMenuId = item.itemId
            menuItem = item
            //binding.drawerLayout.closeDrawer(GravityCompat.START)
            val handled = NavigationUI.onNavDestinationSelected(item, navController)
            if (handled) {
                val parent = binding.navigationView.parent
                if (parent is DrawerLayout) {
                    parent.closeDrawer(binding.navigationView)
                }
            } else {
                val parent = binding.navigationView.parent
                if (parent is DrawerLayout) {
                    parent.closeDrawer(binding.navigationView)
                }
                if (navigationMenuId == R.id.nav_chat) {
                    goToChatActivity()
                }
                if (navigationMenuId == R.id.nav_complain_order_history){
                    showTrackComplainDialog()
                }
                if (navigationMenuId == R.id.nav_logout){
                    logout()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                navigationMenuId = 0
            }
        })
    }

    private fun showTrackComplainDialog() {

        val myDialog = LayoutInflater.from(this).inflate(R.layout.item_view_chat_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(myDialog)
        val mAlertDialog = mBuilder.show()

        val dtCodeEt = mAlertDialog.findViewById<EditText>(R.id.dtCodeEt)

        mAlertDialog.findViewById<AppCompatButton>(R.id.complainBtn)?.setOnClickListener {

            if (isValidDTCodeChat(dtCodeEt?.text.toString())){

                showComplainAddBottomSheet(dtCodeEt?.text.toString())
                mAlertDialog.dismiss()

            } else{
                this.toast("Enter a valid code!")
            }
        }

        mAlertDialog.findViewById<AppCompatButton>(R.id.trackBtn)?.setOnClickListener {
            if (isValidDTCodeChat(dtCodeEt?.text.toString())){
                goToTrackOrderBottomSheet(dtCodeEt?.text.toString())
                mAlertDialog.dismiss()
            } else{
                this.toast("Enter a valid code!")
            }
        }

    }

    private fun showComplainAddBottomSheet(orderId: String) {
        val tag: String = ComplainBottomSheet.tag
        val dialog: ComplainBottomSheet = ComplainBottomSheet.newInstance(orderId)
        dialog.show(supportFragmentManager, tag)
    }

    private fun goToTrackOrderBottomSheet(dtCode: String){
        val tag = TrackOrderBottomSheet.tag
        val dialog = TrackOrderBottomSheet.newInstance(dtCode)
        dialog.show(supportFragmentManager, tag)
    }


    private fun navHeaderData() {
        val navHeaderView = binding.navigationView.getHeaderView(0)
        //val parentHeader: ConstraintLayout = navHeaderView.findViewById(R.id.parent)
        val userPic: ImageView = navHeaderView.findViewById(R.id.userPic)
        val profileEdit: ImageView = navHeaderView.findViewById(R.id.profileEdit)
        val userName: TextView = navHeaderView.findViewById(R.id.name)
        val userPhone: TextView = navHeaderView.findViewById(R.id.phone)
        val appVersion: TextView = navHeaderView.findViewById(R.id.appVersion)

        userName.text = SessionManager.userFullName
        userPhone.text = "${SessionManager.department} (admin level: ${SessionManager.adminType})"

        val options = RequestOptions()
            .placeholder(R.drawable.ic_person_circle)
            .error(R.drawable.ic_person_circle).circleCrop()
            .signature(ObjectKey(Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()))
            .circleCrop()

        Glide.with(this)
            .load(SessionManager.profileImage)
            .apply(options)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(userPic)

        appVersion.text = "v${appVersion()}(${appVersionCode()})"

        profileEdit.setOnClickListener {
            navController.navigate(R.id.nav_profile)
            binding.drawerLayout.closeDrawer(binding.navigationView)
        }
        userPic.setOnClickListener {
            profileEdit.performClick()
        }

        viewModel.updateProfilePic.observe(this, Observer {
            if (it) {
                Glide.with(this)
                    .load(SessionManager.profileImage)
                    .apply(options)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(userPic)
            }
        })
    }

    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        // point is inside view bounds
        return ((x > viewX && x < (viewX + view.width)) && (y > viewY && y < (viewY + view.height)))
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is TextInputEditText || view is EditText) {
                if (!isPointInsideView(event.rawX, event.rawY, view)) {
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun goToChatActivity() {
        val firebaseCredential = FirebaseCredential(
            firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
        )
        val senderData = ChatUserData(SessionManager.dtUserId.toString(), SessionManager.userName, SessionManager.mobile,
            imageUrl = SessionManager.profileImage,
            role = "retention",
            fcmToken = SessionManager.firebaseToken
        )
        val senderData906 = ChatUserData(SessionManager.userId.toString(), SessionManager.userName, SessionManager.mobile,
            imageUrl = SessionManager.profileImage,
            role = "bondhu",
            fcmToken = SessionManager.firebaseToken
        )

        val config = ChatConfigure(
            "dt-retention",
            senderData,
            firebaseCredential = firebaseCredential,
            userType = "user"
        )

        val config906 = ChatConfigure(
            "dt-bondhu",
            senderData906,
            firebaseCredential = firebaseCredential,
            userType = "user"
        )

        // Complain Admin
        /*if (SessionManager.dtUserId == 938) {
            senderData.role = "complain"
            config.documentName = "dt-complain"
        }*/
        if (SessionManager.userId == 906) {
            config906.config(this)
        } else {
            config.config(this)
        }

    }

    fun startLocationUpdate(intervalInMinute: Int = 5, locationDistanceInMeter: Int = 20) {
        if (isLocationPermission()) {
            foregroundService?.setLocationInterval(intervalInMinute)
            foregroundService?.setLocationDifference(locationDistanceInMeter)
            foregroundService?.requestLocationUpdates()
            isLocationServiceRunning = true
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(LocationUpdatesService.ACTION_BROADCAST))
        checkStalledUpdate()
        checkLocationEnable()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, LocationUpdatesService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onStop() {
        if (mBound) {
            unbindService(serviceConnection)
            mBound = false
        }
        ConnectivityReceiver.connectivityReceiverListener = null
        unregisterReceiver(connectivityReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        if (foregroundService != null){
            foregroundService?.removeLocationUpdates()
            isLocationServiceRunning = false
        }
        super.onDestroy()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder =  service as LocationUpdatesService.LocalBinder
            foregroundService = binder.getServerInstance()
            mBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            foregroundService = null
            mBound = false
        }
    }

    inner class MyReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val location: Location? = intent?.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                Timber.tag("LocationLog").d("current location broadcast ${location.latitude},${location.longitude}")
                currentLocation = location
                viewModel.currentLocation.value = location
                SessionManager.latitude = currentLocation?.latitude.toString()
                SessionManager.longitude = currentLocation?.longitude.toString()
                try {
                    val list = geoCoder.getFromLocation(currentLocation?.latitude!!, currentLocation?.longitude!!, 1)
                    if (list.isNotEmpty()) {
                        val addressObj = list.first()
                        lastAddress = addressObj.getAddressLine(0)
                        SessionManager.locationAddress = lastAddress
                        viewModel.addressFromGeoCoder.value = lastAddress
                    }
                } catch (e: Exception) {
                    Timber.tag("LocationLog").d(e)
                }
            }
        }
    }

    fun isLocationPermission(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            return if (permission1 != PackageManager.PERMISSION_GRANTED) {
                val permission1Rationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                if (permission1Rationale) {
                    ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
                } else {
                    ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
                }
                false
            } else {
                true
            }
        } else {
            return true
        }
    }

    fun turnOnGPS() {
        gpsUtils.turnGPSOn {
            isGPS = it
            viewModel.isGPS.value = it
        }
    }

    private fun showLocationConsent() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission1 == PackageManager.PERMISSION_GRANTED) return
        if (SessionManager.isLocationConsentShown) return

        val tag = LocationUsesBottomSheet.tag
        val dialog = LocationUsesBottomSheet.newInstance()
        dialog.show(supportFragmentManager, tag)
        dialog.onItemSelected = { flag ->
            dialog.dismiss()
            if (flag) {
                SessionManager.isLocationConsentShown = true
                if (isLocationPermission()) {
                    turnOnGPS()
                }
            } else {
                this.toast("App need location permission to work properly", Toast.LENGTH_LONG)
            }
        }
    }

    private fun checkLocationEnable() {
        if (SessionManager.isLocationConsentShown) {
            if (isLocationPermission()) {
                turnOnGPS()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                isGPS = true
                viewModel.isGPS.value = true
            } else if (resultCode == Activity.RESULT_CANCELED) {
                isGPS = false
                viewModel.isGPS.value = false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCode -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        turnOnGPS()
                        startLocationUpdate()
                    } else {
                        val permission1Rationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        if (permission1Rationale) {
                                ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)

                        } else {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            Toast.makeText(this,  "ইন্টারনেট কানেকশন সমস্যা হচ্ছে", Toast.LENGTH_SHORT).show()
        } else {
            snackBar?.dismiss()
        }
    }

    private fun appUpdateManager() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,this,requestCodeAppUpdate)
            }
        }
    }

    private fun checkStalledUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            // For IMMEDIATE
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,this,requestCodeAppUpdate)
            }
        }
    }

    private fun checkRemoteConfig() {
        // development only
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60L * 60 * 6
        }
        remoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Timber.d("remoteConfigDebug Config params updated: $updated")
                showForceUpdateDialog()
                showForceSessionCleanDialog()
            } else {
                Timber.d("remoteConfigDebug Fetch failed")
            }
        }
    }

    private fun showForceUpdateDialog() {

        val remoteVersion: Int = remoteConfig.getString("admin_appVersion").toIntOrNull() ?: 0
        val remoteUrl: String = remoteConfig.getString("admin_appUpdateUrl") ?: ""
        Timber.d("remoteConfigDebug $remoteVersion $remoteUrl")
        if (remoteVersion > appVersionCode()) {
            alert("App Update", "Please update app for new feature, bug fix and performance improvement.", true, "Update", "Cancel") { flag ->
                if (flag == AlertDialog.BUTTON_POSITIVE) {
                    try {
                        Intent(Intent.ACTION_VIEW, Uri.parse(remoteUrl)).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }.also {
                            startActivity(it)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.show()
        }
    }

    private fun showForceSessionCleanDialog() {

        val adminSession: String = remoteConfig.getString("admin_loginSession") ?: "0"
        Timber.d("remoteConfigDebug adminSession $adminSession")
        if (SessionManager.sessionId == "0") {
            SessionManager.sessionId = adminSession
        } else if (SessionManager.sessionId != adminSession) {
            alert("Session expire!", "Please login again to update the session", true, "Logout", "Cancel") { flag ->
                if (flag == AlertDialog.BUTTON_POSITIVE) {
                    SessionManager.sessionId = adminSession
                    logout()
                }
            }.show()
        }
    }

    private fun logout() {
        SessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }



}