package com.ajkerdeal.app.ajkerdealadmin.api.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponseItem(
    @SerializedName("UserId")
    var userId: Int = 0,
    @SerializedName("DTUserid")
    var dtUserid: Int = 0,
    @SerializedName("AdminType")
    var adminType: Int = 0,
    @SerializedName("UserName")
    var userName: String? = "",
    @SerializedName("FullName")
    var fullName: String? = "",
    @SerializedName("Email")
    var email: String? = "",
    @SerializedName("Mobile")
    var mobile: String? = "",
    @SerializedName("Address")
    var address: String? = "",
    @SerializedName("Gender")
    var gender: String? = "",
    @SerializedName("Department")
    var department: String? = "",
    @SerializedName("Blood")
    var blood: String? = "",
    @SerializedName("Role")
    var role: String? = ""
)