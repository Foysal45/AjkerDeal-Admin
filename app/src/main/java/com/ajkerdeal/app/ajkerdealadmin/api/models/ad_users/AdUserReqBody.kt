package com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users


import com.google.gson.annotations.SerializedName

data class AdUserReqBody(
    @SerializedName("adminType")
    val adminType: Int = 0,
    @SerializedName("userName")
    val userName: String = ""
)