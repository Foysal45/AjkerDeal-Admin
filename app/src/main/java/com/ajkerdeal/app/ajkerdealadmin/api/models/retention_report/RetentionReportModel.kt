package com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report


import com.google.gson.annotations.SerializedName

data class RetentionReportModel(
    @SerializedName("FullName")
    val fullName: String = "",
    @SerializedName("MerchantCalled")
    val merchantCalled: Int = 0,
    @SerializedName("MerchantVisited")
    val merchantVisited: Int = 0,
    @SerializedName("OrderData")
    val orderData: Int = 0,
    @SerializedName("OrderDataFuture")
    val orderDataFuture: Int = 0,
    @SerializedName("OrderDatanot")
    val orderDatanot: Int = 0,
    @SerializedName("RetentionUserId")
    val retentionUserId: Int = 0
)