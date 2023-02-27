package com.ajkerdeal.app.ajkerdealadmin.api.models.login


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("user")
    var user: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("DeviceId")
    var deviceId: String = "",
    @SerializedName("Token")
    var token: String = ""
)