package com.ajkerdeal.app.ajkerdealadmin.api.models.leave


import com.google.gson.annotations.SerializedName

data class LeaveStatusUpdateRequest(
    @SerializedName("applicationid")
    var applicationid: Int = 0,
    @SerializedName("Status")
    var status: String? = "",
    @SerializedName("UserId")
    var userId: String? = ""
)