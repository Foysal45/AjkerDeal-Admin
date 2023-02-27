package com.ajkerdeal.app.ajkerdealadmin.api.models.leave


import com.google.gson.annotations.SerializedName

data class LeaveSessionRequest(
    @SerializedName("Userid")
    var userid: String? = "",
    @SerializedName("Admintype")
    var admintype: Int = 0,
    @SerializedName("Username")
    var username: String? = "",
    @SerializedName("Name")
    var name: String? = "",
    @SerializedName("LeaveType")
    var leaveType: String? = "",
    @SerializedName("Department")
    var department: String? = "",
    @SerializedName("LeaveStart")
    var leaveStart: String? = "",
    @SerializedName("LeaveEnd")
    var leaveEnd: String? = "",
    @SerializedName("Reason")
    var reason: String? = "",
    @SerializedName("ResumptionDate")
    var resumptionDate: String? = "",
    @SerializedName("Mobile")
    var mobile: String? = "",
    @SerializedName("Status")
    var status: String? = ""
)