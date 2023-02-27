package com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users


import com.google.gson.annotations.SerializedName

data class ADUsersModel(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("adminType")
    val adminType: Int = 0,
    @SerializedName("allowOutsideAccess")
    val allowOutsideAccess: Int = 0,
    @SerializedName("bloodGroup")
    val bloodGroup: String = "",
    @SerializedName("fullName")
    val fullName: String = "",
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("isAcquisition")
    val isAcquisition: Boolean = false,
    @SerializedName("isActive")
    val isActive: Int = 0,
    @SerializedName("isRetention")
    val isRetention: Boolean = false,
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("passwrd")
    val passwrd: String = "",
    @SerializedName("personalEmail")
    val personalEmail: String = "",
    @SerializedName("postedOn")
    val postedOn: String = "",
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("userName")
    val userName: String = ""
)