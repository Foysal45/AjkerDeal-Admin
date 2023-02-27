package com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend


import com.google.gson.annotations.SerializedName

data class LeaveRecommendStatusUpdateReq(
    @SerializedName("applicationid")
    var applicationid: Int = 0,
    @SerializedName("Recommend")
    var recommend: Boolean = false,
    @SerializedName("UserId")
    var userId: String = ""
)