package com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock


import com.google.gson.annotations.SerializedName

data class TokenGenerateUserLoginRequest(
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("password")
    var password: String = ""
)