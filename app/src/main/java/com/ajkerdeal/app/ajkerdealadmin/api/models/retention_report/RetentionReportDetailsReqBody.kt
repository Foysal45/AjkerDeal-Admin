package com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report


import com.google.gson.annotations.SerializedName

data class RetentionReportDetailsReqBody(
    @SerializedName("DateFlag")
    val dateFlag: Int = 0,
    @SerializedName("FromDate")
    val fromDate: String = "",
    @SerializedName("RetentionUserId")
    val retentionUserId: Int = 0,
    @SerializedName("Index")
    val Index: Int = 0,
    @SerializedName("Count")
    val Count: Int = 0

)