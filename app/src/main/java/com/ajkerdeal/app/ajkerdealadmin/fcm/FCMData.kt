package com.ajkerdeal.app.ajkerdealadmin.fcm


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * {notificationType}
 * "0" default
 * "1" big text
 * "2" banner
 * "3" big text with size image
 * "" else
 */
@Parcelize
data class FCMData(
    @SerializedName("notificationType")
    var notificationType: String? = "",
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("body")
    var body: String? = "",
    @SerializedName("imageUrl")
    var imageUrl: String? = "",
    @SerializedName("bigText")
    var bigText: String? = "",

    @SerializedName("senderId")
    var senderId: String? = "",

    @SerializedName("senderName")
    var senderName: String? = "",

    @SerializedName("senderRole")
    var senderRole: String? = "",

    @SerializedName("receiverId")
    var receiverId: String? = "",
): Parcelable