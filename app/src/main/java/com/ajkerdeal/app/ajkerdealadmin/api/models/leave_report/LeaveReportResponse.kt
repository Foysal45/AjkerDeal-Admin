package com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report


import com.google.gson.annotations.SerializedName

data class LeaveReportResponse(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("data")
    var `data`: List<LeaveData>? = listOf()
)