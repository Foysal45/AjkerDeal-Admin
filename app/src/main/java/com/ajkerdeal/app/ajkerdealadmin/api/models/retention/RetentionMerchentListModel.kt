package com.ajkerdeal.app.ajkerdealadmin.api.models.retention


import com.google.gson.annotations.SerializedName

data class RetentionMerchentListModel(
    @SerializedName("Address")
    val address: String = "",
    @SerializedName("AlterMobile")
    val alterMobile: String = "",
    @SerializedName("BkashNumber")
    val bkashNumber: String = "",
    @SerializedName("CompanyName")
    val companyName: String = "",
    @SerializedName("CourierUserId")
    val courierUserId: Int = 0,
    @SerializedName("LastOrderDate")
    val lastOrderDate: String = "",
    @SerializedName("Mobile")
    val mobile: String = "",
    @SerializedName("TotalComplain")
    val totalComplain: Int = 0,
    @SerializedName("TotalOrder")
    val totalOrder: Int = 0,
    @SerializedName("Password")
    val password: String = ""
)