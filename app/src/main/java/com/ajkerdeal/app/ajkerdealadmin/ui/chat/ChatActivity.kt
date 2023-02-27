package com.ajkerdeal.app.ajkerdealadmin.ui.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential
import com.ajkerdeal.app.ajkerdealadmin.databinding.ActivityChatBinding
import com.ajkerdeal.app.ajkerdealadmin.fcm.FCMData
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeFragment
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.history.ChatHistoryFragment
import com.ajkerdeal.app.ajkerdealadmin.ui.login.LoginActivity
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.history.ChatHistoryFragmentWithSeenUnseenTab

class ChatActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private var credential: FirebaseCredential? = null
    private var documentName: String? = null
    private var userType: String? = null
    private var sender: ChatUserData? = null
    private var receiver: ChatUserData? = null
    private lateinit var firebaseApp: FirebaseApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        if (!SessionManager.isLogin) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (SessionManager.userId == 906){
            sender = ChatUserData("906", SessionManager.userName, SessionManager.mobile,
                imageUrl = "",
                role = "bondhu",
                fcmToken = SessionManager.firebaseToken
            )
        }else{
            sender = ChatUserData(SessionManager.dtUserId.toString(), SessionManager.userName, SessionManager.mobile,
                imageUrl = SessionManager.profileImage,
                role = "retention",
                fcmToken = SessionManager.firebaseToken
            )
        }

        credential = FirebaseCredential(firebaseWebApiKey = BuildConfig.FirebaseWebApiKey)

        //val firebaseApp = initFirebaseDatabase(credential)

        firebaseApp = Firebase.app
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent ?: return
        val bundle = intent.extras
        if (bundle != null) {
            val model: FCMData? = bundle.getParcelable("data")
            val notificationType = bundle.getString("notificationType") ?: ""
            if (notificationType.isNotEmpty()) {
                if (model != null) {
                    // From foreground
                    documentName = bundle.getString("notificationType")
                    sender?.role = when (documentName) {
                        "dt-retention" -> "retention"
                        "dt-complain" -> "complain"
                        "dt-bondhu" -> "bondhu"
                        else -> "retention"
                    }
                    receiver = ChatUserData(
                        model.senderId ?: "",
                        model.senderName ?: "", "", "", "",
                        model.senderRole ?: ""
                    )
                } else {
                    // From background
                    documentName = bundle.getString("notificationType")
                    sender?.role = when (documentName) {
                        "dt-retention" -> "retention"
                        "dt-complain" -> "complain"
                        "dt-bondhu" -> "bondhu"
                        else -> "retention"
                    }
                    receiver = ChatUserData(
                        bundle.getString("senderId") ?: "",
                        bundle.getString("senderName") ?: "", "", "", "",
                        bundle.getString("senderRole") ?: ""
                    )
                }
                //credential = FirebaseCredential(firebaseWebApiKey = BuildConfig.FirebaseWebApiKey)
            } else {
                // From home
                bundle.getBundle("chatConfig")?.run {
                    documentName = getString("documentName")
                    userType = getString("userType")
                    receiver = getParcelable("receiver")
                    credential = getParcelable("credential")
                    sender = getParcelable("sender")
                    sender?.role = when (documentName) {
                        "dt-retention" -> "retention"
                        "dt-complain" -> "complain"
                        "dt-bondhu" -> "bondhu"
                        else -> "retention"
                    }
                }
            }
            navigation()
        }
        intent.removeExtra("notificationType")
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun navigation() {

        if(sender?.id == SessionManager.dtUserId.toString() || Integer.parseInt(sender?.id ?: "" ) == 906){
            if (Integer.parseInt(sender?.id ?: "-1") == 906){
                if (SessionManager.dtUserId == 1367){
                    if (receiver?.id.isNullOrEmpty()) {
                        val bundleChatHistory = bundleOf(
                            "documentName" to documentName,
                            "firebaseStorageUrl" to credential?.storageUrl,
                            "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                            "sender" to sender
                        )
                        goToChatHistory(firebaseApp, bundleChatHistory)
                    } else {
                        val bundleChatCompose = bundleOf(
                            "documentName" to documentName,
                            "firebaseStorageUrl" to credential?.storageUrl,
                            "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                            "sender" to sender,
                            "receiver" to receiver
                        )
                        //goToChatCompose(firebaseApp, bundleChatCompose)
                        goToChatComposeWithHistory(firebaseApp, bundleChatCompose)
                    }
                } else {
                    if (receiver?.id.isNullOrEmpty()) {
                        val bundleChatHistory = bundleOf(
                            "documentName" to documentName,
                            "firebaseStorageUrl" to credential?.storageUrl,
                            "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                            "sender" to sender
                        )
                        goToChatHistoryWithSeenUnseen(firebaseApp, bundleChatHistory)
                    } else {
                        val bundleChatCompose = bundleOf(
                            "documentName" to documentName,
                            "firebaseStorageUrl" to credential?.storageUrl,
                            "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                            "sender" to sender,
                            "receiver" to receiver
                        )
                        //goToChatCompose(firebaseApp, bundleChatCompose)
                        goToChatComposeWithHistory(firebaseApp, bundleChatCompose)
                    }
                }
            } else {
                if (receiver?.id.isNullOrEmpty()) {
                    val bundleChatHistory = bundleOf(
                        "documentName" to documentName,
                        "firebaseStorageUrl" to credential?.storageUrl,
                        "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                        "sender" to sender
                    )
                    goToChatHistory(firebaseApp, bundleChatHistory)
                } else {
                    val bundleChatCompose = bundleOf(
                        "documentName" to documentName,
                        "firebaseStorageUrl" to credential?.storageUrl,
                        "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                        "sender" to sender,
                        "receiver" to receiver
                    )
                    //goToChatCompose(firebaseApp, bundleChatCompose)
                    goToChatComposeWithHistory(firebaseApp, bundleChatCompose)
                }
            }

        } else{
            if (receiver?.id.isNullOrEmpty()) {
                val bundleChatHistory = bundleOf(
                    "documentName" to documentName,
                    "firebaseStorageUrl" to credential?.storageUrl,
                    "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                    "sender" to sender
                )
                goToChatHistoryWithSeenUnseen(firebaseApp, bundleChatHistory)
            } else {
                val bundleChatCompose = bundleOf(
                    "documentName" to documentName,
                    "firebaseStorageUrl" to credential?.storageUrl,
                    "firebaseWebApiKey" to credential?.firebaseWebApiKey,
                    "sender" to sender,
                    "receiver" to receiver
                )
                //goToChatCompose(firebaseApp, bundleChatCompose)
                goToChatComposeWithHistory(firebaseApp, bundleChatCompose)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.addOnBackStackChangedListener(onBackStackChangeLister)
    }

    override fun onStop() {
        super.onStop()
        supportFragmentManager.removeOnBackStackChangedListener(onBackStackChangeLister)
    }

    private val onBackStackChangeLister = FragmentManager.OnBackStackChangedListener {
        when (supportFragmentManager.findFragmentById(R.id.container)) {
            is ChatHistoryFragment -> {
                setToolbar("Chat History")
            }
            /*is ChatComposeFragment -> {
                setToolbar(receiver?.name ?: "Chat")
            }*/
        }
    }

    private fun initFirebaseDatabase(credential: FirebaseCredential): FirebaseApp {
        return try {
            val options = FirebaseOptions.Builder()
                .setProjectId(credential.projectId)
                .setApplicationId(credential.applicationId)
                .setApiKey(credential.apikey)
                .setDatabaseUrl(credential.databaseUrl)
                .build()

            FirebaseApp.initializeApp(this, options, "Ajkerdeal Seller")
            FirebaseApp.getInstance("Ajkerdeal Seller")
        } catch (e: Exception) {
            FirebaseApp.getInstance("Ajkerdeal Seller")
        }
    }

    private fun goToChatCompose(firebaseApp: FirebaseApp, bundle: Bundle) {
        val fragment = ChatComposeFragment.newInstance(firebaseApp, bundle)
        val tag = ChatComposeFragment.tag
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment, tag)
            commit()
        }
    }

    private fun goToChatHistory(firebaseApp: FirebaseApp, bundle: Bundle) {
        val fragment = ChatHistoryFragment.newInstance(firebaseApp, bundle)
        val tag = ChatHistoryFragment.tag
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment, tag)
            commit()
        }
    }

    private fun goToChatHistoryWithSeenUnseen(firebaseApp: FirebaseApp, bundle: Bundle) {
        val fragment = ChatHistoryFragmentWithSeenUnseenTab.newInstance(firebaseApp, bundle)
        val tag = ChatHistoryFragmentWithSeenUnseenTab.tag
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment, tag)
            commit()
        }
    }

    private fun goToChatComposeWithHistory(firebaseApp: FirebaseApp, bundle: Bundle) {
        val fragment1 = ChatHistoryFragment.newInstance(firebaseApp, bundle)
        val tag1 = ChatHistoryFragment.tag
        val fragment2 = ChatComposeFragment.newInstance(firebaseApp, bundle)
        val tag2 = ChatComposeFragment.tag
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment1, tag1)
            commit()
        }
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, fragment2, tag2)
            addToBackStack(tag2)
            commit()
        }
    }

    fun setToolbar(title: String) {
        supportActionBar?.title = title
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_toolbar_chat, menu)

        val item = menu!!.findItem(R.id.action_track_complain)

        *//*if (something){
            item.isVisible = false
        }*//*

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.itemId
        return if (id == R.id.action_track_complain) {

            val myDialog = LayoutInflater.from(this).inflate(R.layout.item_view_chat_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(myDialog)
            val mAlertDialog = mBuilder.show()

            val dtCodeEt = mAlertDialog.findViewById<EditText>(R.id.dtCodeEt)

            mAlertDialog.findViewById<AppCompatButton>(R.id.complainBtn)?.setOnClickListener {

                if (isValidDTCodeChat(dtCodeEt?.text.toString())){

                    val fragment: ComplainFragment = ComplainFragment.newInstance(dtCodeEt?.text.toString(), "DT Complain")
                    val ft: FragmentTransaction = this.supportFragmentManager.beginTransaction()
                    ft.replace(R.id.container, fragment, "ComplainFragment")
                    ft.addToBackStack("ComplainFragment")
                    ft.commit()
                    mAlertDialog.dismiss()

                } else{
                    this.toast("Enter a valid code!")
                }
            }

            mAlertDialog.findViewById<AppCompatButton>(R.id.trackBtn)?.setOnClickListener {
                if (isValidDTCodeChat(dtCodeEt?.text.toString())){
                    val tag = TrackOrderBottomSheet.tag
                    val dialog = TrackOrderBottomSheet.newInstance(dtCodeEt?.text.toString())
                    dialog.show(this.supportFragmentManager, tag)
                    mAlertDialog.dismiss()
                } else{
                    this.toast("Enter a valid code!")
                }
            }
            true

        } else super.onOptionsItemSelected(item)

    }*/

}