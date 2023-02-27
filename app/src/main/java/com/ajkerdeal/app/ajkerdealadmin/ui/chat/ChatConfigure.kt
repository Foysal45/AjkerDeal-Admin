package com.ajkerdeal.app.ajkerdealadmin.ui.chat

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential

class ChatConfigure constructor(
    var documentName: String,
    private val sender: ChatUserData,
    private val receiver: ChatUserData = ChatUserData(),
    private val firebaseCredential: FirebaseCredential? = null,
    private val userType: String
) {

    fun config(context: Context) {
        Intent(context, ChatActivity::class.java).apply {
            val bundle = bundleOf(
                "credential" to firebaseCredential,
                "documentName" to documentName,
                "sender" to sender,
                "receiver" to receiver,
                "userType" to userType
            )
            putExtra("chatConfig", bundle)
        }.also {
            context.startActivity(it)
        }
    }
    
}