package com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report

import com.google.gson.annotations.SerializedName

data class RetentionReportReqBody(
    @SerializedName("fromDate")
    val fromDate: String = "",
    @SerializedName("Index")
    val Index: Int = 0,
    @SerializedName("Count")
    val Count: Int = 0
)