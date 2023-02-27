package com.ajkerdeal.app.ajkerdealadmin.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.BreakRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeSessionStartStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session.LastDaySessionStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMDataModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMNotification
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMRequest
import com.ajkerdeal.app.ajkerdealadmin.broadcast.ReminderBroadcast
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentHomeBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.employee_leave.LeaveApplicationViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet.BreakReasonBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet.StartWorkingBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet.WorkUpdateBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.last_days_session.LastDaysSessionEndBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by inject()
    private val viewModelHomeActivity: HomeActivityViewModel by inject()
    private val viewModelLeave: LeaveApplicationViewModel by inject()
    private val viewModelSendPushNotification: ChatComposeViewModel by inject()

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    private var isStartWorking: Boolean = false
    private var isInBreak: Boolean = false
    private var isGPS: Boolean = false
    private val firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment().apply {}
        val tag: String = HomeFragment::class.java.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        profileInfo()
        fetchWorkStatus()
        fetchEmployeeStatus()
        fetchEmployeeBreakStatus()
        initClickLister()
    }

    override fun onResume() {
        super.onResume()
        //checkLocationEnable()

        val options = RequestOptions()
            .placeholder(R.drawable.ic_person_circle)
            .error(R.drawable.ic_person_circle).circleCrop()
            .signature(ObjectKey(Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()))
            .circleCrop()

        viewModelHomeActivity.updateProfilePic.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding?.profile?.let { view ->
                    Glide.with(view)
                        .load(SessionManager.profileImage)
                        .apply(options)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(view)
                }
            }
        })
    }

    private fun init() {
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun fetchWorkStatus() {
        viewModel.getEmployeeWorkingTime(SessionManager.userId).observe(viewLifecycleOwner, Observer { model->
            binding?.swipeRefresh?.isRefreshing = false
            binding?.startWorkTime?.text = "Start Time:   ${model.startTime}"
            binding?.endtWorkTime?.text = "End Time:   ${if (model.endTime.isNullOrEmpty()) "-" else model.endTime}"
            binding?.workHourStatus?.text = model.workingTime
            binding?.breakHourStatus?.text = model.breakTime
        })
    }

    fun profileInfo() {
        binding?.name?.text = SessionManager.userFullName
        binding?.email?.text = SessionManager.email
        binding?.mobile?.text = SessionManager.mobile
        binding?.address?.text = SessionManager.address
        binding?.userIdAD?.text = "AD: ${SessionManager.userId}"
        binding?.userIdDT?.text = "DT: ${SessionManager.dtUserId}"

        viewModelHomeActivity.addressFromGeoCoder.observe(viewLifecycleOwner, Observer { location->

            if (!location.isNullOrEmpty()){
                binding?.currentLocation?.text = location
            } else {
                binding?.currentLocation?.text = SessionManager.locationAddress
            }

        })

        val options = RequestOptions()
            .placeholder(R.drawable.ic_person_circle)
            .error(R.drawable.ic_person_circle).circleCrop()
            .signature(ObjectKey(Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()))
            .circleCrop()

        binding?.profile?.let { view ->
            Glide.with(view)
                .load(SessionManager.profileImage)
                .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(view)
        }

        viewModelHomeActivity.isGPS.observe(viewLifecycleOwner, Observer {
            isGPS = it
        })
    }

    private fun fetchEmployeeStatus() {
        viewModel.getEmployeeStatus(SessionManager.userId).observe(viewLifecycleOwner, Observer { model ->
            SessionManager.isPresent = model.IsPresent
            if (model.IsPresent) {
                isStartWorking = true
                binding?.workStartEndBtn?.text = "End Work"
                binding?.workBreakEndBtnLayout?.visibility = View.VISIBLE
                binding?.workStartEndBtn?.setBackgroundColor(Color.RED)
            } else {
                isStartWorking = false
                binding?.workBreakEndBtnLayout?.visibility = View.GONE
                binding?.workStartEndBtn?.text = "Start Work"
                binding?.workStartEndBtn?.setBackgroundColor(Color.parseColor("#056608"))
            }
            val status = if (isStartWorking) "Working" else "Idle"
            binding?.workStatus?.text = "Current work status: $status"
            binding?.workStartEndBtn?.isEnabled = true
        })
    }

    private fun fetchEmployeeBreakStatus(){
        viewModel.getEmployeeIsBreakStatus(SessionManager.userId).observe(viewLifecycleOwner, Observer { flag ->
            Timber.d("breakDebug $flag")
            isInBreak = flag
            updateUIForBreakStatus()
        })
    }

    private fun initClickLister() {

        binding?.workStartEndBtn?.setOnClickListener {
            if (isLocationPermission() && checkLocationEnable()) {
                if (isStartWorking){

                    if (isInBreak){
                        showBreakSessionRunningDialog()
                    }else{
                        showWorkUpdateBottomSheet()
                    }
                }else{
                    showStartWorkBottomSheet()
                    //showWorkStartPopup()
                }
            } else {
                context?.toast("Please give location permission")
            }
        }

        binding?.workBreakEndBtnLayout?.setOnClickListener {
            if (isLocationPermission() && checkLocationEnable()) {
                if (isInBreak) {
                    updateBreakStatus()
                }else{
                    showBreakReasonBottomSheet()
                }
            } else {
                context?.toast("Please give location permission")
            }
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            fetchWorkStatus()
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.ShowMessage -> {
                    requireContext().toast(state.message)
                }
                is ViewState.KeyboardState -> {
                    hideKeyboard()
                }
                is ViewState.ProgressState -> {
                    if (state.isShow) {
                        binding?.progressBar?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun showStartWorkBottomSheet() {
        val tag: String = StartWorkingBottomSheet.tag
        val dialog: StartWorkingBottomSheet = StartWorkingBottomSheet.newInstance()
        dialog.show(childFragmentManager, tag)
        dialog.onWorkingPlaceTypeClicked = { workPlace ->
            dialog.dismiss()
            SessionManager.workFrom = workPlace
            updateWorkStatus("")
        }
    }

    private fun showWorkUpdateBottomSheet() {
        val tag: String = WorkUpdateBottomSheet.tag
        val dialog: WorkUpdateBottomSheet = WorkUpdateBottomSheet.newInstance()
        dialog.show(childFragmentManager, tag)
        dialog.onWOrkUpdateClicked = { report ->
            dialog.dismiss()
            updateWorkStatus(report)
        }
    }


    private fun showLastDaySessionEndBottomSheet(report: String) {
        val tag: String = LastDaysSessionEndBottomSheet.tag
        val dialog: LastDaysSessionEndBottomSheet = LastDaysSessionEndBottomSheet.newInstance(report)
        dialog.show(childFragmentManager, tag)
        dialog.onEmergencyEndClicked = { time, workReport ->
            dialog.dismiss()
            lastDaySessionEnd(time, workReport)
        }
    }

    private fun showBreakReasonBottomSheet() {
        val tag: String = BreakReasonBottomSheet.tag
        val dialog: BreakReasonBottomSheet = BreakReasonBottomSheet.newInstance()
        dialog.show(childFragmentManager, tag)
        dialog.onTakingBreakClicked = { breakReason ->
            dialog.dismiss()
            updateBreakStatus(breakReason)
        }
    }

    private fun updateWorkStatus(report: String) {
        if (isLocationPermission() && checkLocationEnable()) {
            val requestBody = if (isStartWorking) {
                EmployeeSessionStartStopRequest(
                    SessionManager.userId.toString(),
                    SessionManager.userFullName,
                    SessionManager.workFrom,
                    isStartWorking,
                    "0.0",
                    "0.0",
                    "",
                    report,
                    SessionManager.latitude,
                    SessionManager.longitude,
                    SessionManager.locationAddress,
                    SessionManager.department,
                    appVersion()
                )
            } else {
                EmployeeSessionStartStopRequest(
                    SessionManager.userId.toString(),
                    SessionManager.userFullName,
                    SessionManager.workFrom,
                    isStartWorking,
                    SessionManager.latitude,
                    SessionManager.longitude,
                    SessionManager.locationAddress,
                    "",
                    "0.0",
                    "0.0",
                    "",
                    SessionManager.department,
                    appVersion()
                )
            }
            viewModel.startStopEmployeeSession(requestBody).observe(viewLifecycleOwner, Observer { model ->
                Timber.d("startStopEmployeeSession $model")
                if (model.status) {
                    val msg = if (isStartWorking) {
                        cancelNotification()
                        //cancelReminder()
                        "Work session ended"
                    } else {
                        createNotification("Work session running")
                        //createReminder()
                        if(SessionManager.adminType == 11){
                            sendPushNotification(1, requestBody.from)
                        }
                        "Work session started"
                    }
                    binding?.parent?.snackbar(msg)
                    fetchEmployeeStatus()
                    fetchWorkStatus()
                } else {
                    showLastDaySessionEndBottomSheet(report)
                }
            })
        } else {
            context?.toast("Please give location permission")
        }
    }

    private fun lastDaySessionEnd(time: String, report: String) {
        val requestBody = LastDaySessionStopRequest(SessionManager.userId.toString(), time, report)
        viewModel.lastSessionEmergencyEnd(requestBody).observe(viewLifecycleOwner, Observer { model->
            if (model.status){
                binding?.swipeRefresh?.isRefreshing = false
                fetchEmployeeStatus()
            }else{
                binding?.parent?.snackbar("Something went wrong!")
            }

        })
    }

    private fun updateBreakStatus(breakReason: String = "") {
        val requestBody = BreakRequest(SessionManager.userId, breakReason)
        viewModel.startEmployeeBreakSession(requestBody).observe(viewLifecycleOwner, Observer { model ->
            if (model.status) {
                isInBreak = !isInBreak
                updateUIForBreakStatus()
                val msg = if (isInBreak) {
                    updateNotification("Break session running")
                    "Taking break right now"
                } else {
                    updateNotification("Work session running")
                    "Back to work"
                }
                binding?.parent?.snackbar(msg)
                fetchWorkStatus()
            }
        })
    }

    private fun updateUIForBreakStatus() {
        if (isInBreak){
            breakLayoutUpdated()
        }else{
            notInBreakLayoutUpdated()
        }
    }

    private fun checkLocationEnable(): Boolean {
        return if (isGPS) {
            true
        } else {
            (activity as HomeActivity).turnOnGPS()
            false
        }
    }

    private fun notInBreakLayoutUpdated(){
        binding?.workBreakEndBtnLayout?.setBackgroundResource(R.drawable.bg_stroke)
        binding?.workBreakEndBtn?.text = "Break"
        binding?.workBreakEndBtn?.setTextColor(Color.parseColor("#056608"))
        binding?.workBreakEndImageView?.setImageResource(R.drawable.ic_start)
        binding?.workBreakEndImageView?.setColorFilter(Color.parseColor("#056608"))

        Timber.d("breakDebug onWork")
    }

    private fun breakLayoutUpdated(){
        binding?.workBreakEndBtnLayout?.setBackgroundResource(R.drawable.bg_stroke_red)
        binding?.workBreakEndBtn?.text = "Back"
        binding?.workBreakEndBtn?.setTextColor(Color.RED)
        binding?.workBreakEndImageView?.setImageResource(R.drawable.ic_pause)
        binding?.workBreakEndImageView?.setColorFilter(Color.RED)

        Timber.d("breakDebug onBreak")
    }

    private fun isLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission1 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                showLocationPermissionDialog()
                false
            } else true
        } else {
            true
        }

    }

    private fun showLocationPermissionDialog() {
        alert("লোকেশন পারমিশন", "আপনার বর্তমান লোকেশন জানা অত্যন্ত জরুরি, লোকেশন পারমিশন দিন", true, "ঠিক আছে", "পরে দিবো") {
            if (it == AlertDialog.BUTTON_POSITIVE) {
                if (activity != null) {
                    (activity as HomeActivity).isLocationPermission()
                }
            }
        }.show()
    }

    private fun showBreakSessionRunningDialog() {
        alert("You are in break now.", "Please, back from break to end work.", false, "Ok", "Cancel").show()
    }

    private fun createNotification(title: String, body: String = "Tab to open app") {

        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 21401, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder = NotificationCompat.Builder(requireContext(), getString(R.string.notification_channel_id_session))
        with(notificationBuilder!!){
            setSmallIcon(R.drawable.ic_notification_ad)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(false)
            color = ContextCompat.getColor(requireContext(), R.color.colorPrimaryVariant)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(pendingIntent)
            setOngoing(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            val channel = NotificationChannel(getString(R.string.notification_channel_id_session), getString(R.string.notification_channel_name_session), NotificationManager.IMPORTANCE_DEFAULT)
            with(channel){
                description = getString(R.string.notification_channel_des_session)
                setShowBadge(true)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                setSound(soundUri, audioAttributes)
            }
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = notificationBuilder!!.build()
        notificationManager?.notify(21402, notification)
    }

    private fun updateNotification(title: String, body: String = "Tab to open app") {
        notificationBuilder?.let {
            it.setContentTitle(title)
            it.setContentText(body)
        }
        notificationManager?.notify(21402, notificationBuilder?.build())
    }

    private fun cancelNotification() {
        notificationManager?.cancel(21402)
    }

    private fun createReminder() {
        val intent = Intent(requireContext(), ReminderBroadcast::class.java).apply {
            putExtra("title", "Work session on going")
            putExtra("body", "Press End Work btn to stop today's session")
        }
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), SessionManager.userId, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager: AlarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmTime = System.currentTimeMillis() + (1000 * 60 * 60 * 9) // 9 hours
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        Timber.d("reminderDebug createReminder for ${SessionManager.userId}")
    }

    private fun cancelReminder() {
        val intent = Intent(requireContext(), ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), SessionManager.userId, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager: AlarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Timber.d("reminderDebug cancelReminder for ${SessionManager.userId}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun sendPushNotification(userId: Int, from: String) {
        viewModelLeave.userfirebasetoken(userId).observe(viewLifecycleOwner, Observer{ token ->
            if (!(token == null || firebaseWebApiKey.isNullOrEmpty())) {
                val title = SessionManager.userFullName  + " started working"
                val sdf = SimpleDateFormat("hh:mm a", Locale.US)
                val body = "From: " +  from  +" Time: "+  sdf.format( Calendar.getInstance().time)
                val notificationModel = FCMNotification(title, body, "", "")
                val requestBody = FCMRequest(token.firebasetoken!!, notificationModel, data = FCMDataModel(notificationType = "attendance", title = title, body = body, receiverId = userId.toString()))
                viewModelSendPushNotification.sendPushNotifications(firebaseWebApiKey, requestBody)
                findNavController().popBackStack()
            }
        })
    }

}