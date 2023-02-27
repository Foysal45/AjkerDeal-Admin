package com.ajkerdeal.app.ajkerdealadmin.api.models.profile


import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponse(
    @SerializedName("msg")
    var msg: String? = "",
    @SerializedName("status")
    var status: Boolean = false
)