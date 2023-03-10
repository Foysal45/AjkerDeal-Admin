package com.ajkerdeal.app.ajkerdealadmin.api.models.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatUserData(
    var id: String = "",
    var name: String = "",
    var mobile: String = "",
    var email: String = "",
    var imageUrl: String = "",
    var role: String = "",
    var fcmToken: String = "",
    var status: Int = 0,
    var currentRoomId: String = ""
): Parcelable
