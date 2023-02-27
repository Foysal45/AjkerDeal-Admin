package com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report


import com.google.gson.annotations.SerializedName

data class RetentionReportDetailsModel(
    @SerializedName("CallDuration")
    val callDuration: Double = 0.0,
    @SerializedName("CalledDate")
    val calledDate: String = "",
    @SerializedName("CalledSummary")
    val calledSummary: String = "",
    @SerializedName("CompanyName")
    val companyName: String = "",
    @SerializedName("CourierUserId")
    val courierUserId: Int = 0,
    @SerializedName("MerchantId")
    val merchantId: Int = 0,
    @SerializedName("Mobile")
    val mobile: String = "",
    @SerializedName("UserName")
    val userName: String = "",
    @SerializedName("VisitedDate")
    val visitedDate: String = "",
    @SerializedName("VisitedSummary")
    val visitedSummary: String = ""
)