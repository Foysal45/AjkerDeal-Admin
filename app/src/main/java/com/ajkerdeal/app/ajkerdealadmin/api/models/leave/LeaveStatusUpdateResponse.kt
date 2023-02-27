package com.ajkerdeal.app.ajkerdealadmin.api.models.leave


import com.google.gson.annotations.SerializedName

data class LeaveStatusUpdateResponse(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("msg")
    var msg: String = ""
)