package com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend


import com.google.gson.annotations.SerializedName

data class LeaveRecommendStatusUpdateResponse(
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("status")
    var status: Boolean = false
)