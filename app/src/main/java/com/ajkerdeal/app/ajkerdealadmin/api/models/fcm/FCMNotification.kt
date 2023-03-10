package com.ajkerdeal.app.ajkerdealadmin.api.models.fcm


import com.google.gson.annotations.SerializedName

data class FCMNotification(
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("body")
    var body: String? = "",
    @SerializedName("image")
    var image: String? = "",
    @SerializedName("click_action")
    var clickAction: String? = ""
)