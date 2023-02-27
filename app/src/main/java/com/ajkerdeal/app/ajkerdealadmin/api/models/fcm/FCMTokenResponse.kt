package com.ajkerdeal.app.ajkerdealadmin.api.models.fcm


import com.google.gson.annotations.SerializedName

data class FCMTokenResponse(
    @SerializedName("firebasetoken")
    var firebasetoken: String? = ""
)