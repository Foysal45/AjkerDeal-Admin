package com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.leave_count


import com.google.gson.annotations.SerializedName

data class LeaveCountResponse(
    @SerializedName("data")
    var `data`: List<LeaveCountData>? = listOf(),
    @SerializedName("status")
    var status: Boolean = false
)