package com.ajkerdeal.app.ajkerdealadmin.api.models.login


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("body")
    var body: LoginResponseItem
)