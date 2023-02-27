package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TeleSalesActiveMerchantDetailsResponse(
    @SerializedName("courierUserId")
    val courierUserId: Int = 0,
    @SerializedName("alterMobile")
    val alterMobile: String = "",
    @SerializedName("bkashNumber")
    val bkashNumber: String = "",
    @SerializedName("companyName")
    val companyName: String = "",
    @SerializedName("lastOrderDate")
    val lastOrderDate: String = "",
    @SerializedName("mobile")
    val mobile: String = ""
)