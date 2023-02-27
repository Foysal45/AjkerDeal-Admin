package com.ajkerdeal.app.ajkerdealadmin.api.models.report


import com.google.gson.annotations.SerializedName

data class WorkReportData(
    @SerializedName("From")
    var from: String? = "",
    @SerializedName("Break Time")
    var breakTime: String? = "",
    @SerializedName("Session")
    var session: List<SessionData>? = listOf(),
    @SerializedName("Date")
    var date: String? = "",
    @SerializedName("UserName")
    var userName: String? = "",
    @SerializedName("Working Time")
    var workingTime: String? = "",
    @SerializedName("Userid")
    var userid: Int = 0
)