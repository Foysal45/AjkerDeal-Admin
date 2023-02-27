package com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock


import com.google.gson.annotations.SerializedName

data class AdminUsers(
    @SerializedName("address")
    var address: Any = Any(),
    @SerializedName("adminType")
    var adminType: Int = 0,
    @SerializedName("bloodGroup")
    var bloodGroup: Any = Any(),
    @SerializedName("email")
    var email: Any = Any(),
    @SerializedName("fullName")
    var fullName: String = "",
    @SerializedName("gender")
    var gender: Any = Any(),
    @SerializedName("isActive")
    var isActive: Int = 0,
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("passwrd")
    var passwrd: Any = Any(),
    @SerializedName("token")
    var token: Any = Any(),
    @SerializedName("userId")
    var userId: Int = 0,
    @SerializedName("userName")
    var userName: Any = Any()
)