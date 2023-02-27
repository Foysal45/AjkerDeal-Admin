package com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.leave_count


import com.google.gson.annotations.SerializedName

data class LeaveCountData(
    @SerializedName("UserId")
    var userId: String? = "",
    @SerializedName("Sick_Leave")
    var sickLeave: Int = 0,
    @SerializedName("Casual_Leave")
    var casualLeave: Int = 0
)