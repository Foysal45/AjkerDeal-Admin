package com.ajkerdeal.app.ajkerdealadmin.api.models.leave


import com.google.gson.annotations.SerializedName

data class LeaveListRequest(
    @SerializedName("UserId")
    var userId: String? = "",
    @SerializedName("Department")
    var department: String? = "",
    @SerializedName("Role")
    var role: String? = ""
)