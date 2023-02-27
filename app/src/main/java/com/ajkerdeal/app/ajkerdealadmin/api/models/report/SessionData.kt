package com.ajkerdeal.app.ajkerdealadmin.api.models.report


import com.google.gson.annotations.SerializedName

data class SessionData(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("UserId")
    var userId: Int = 0,
    @SerializedName("UserName")
    var userName: String? = "",
    @SerializedName("IsBreak")
    var isBreak: Boolean = false,
    @SerializedName("IsPresent")
    var isPresent: Boolean = false,
    @SerializedName("From")
    var from: String? = "",
    @SerializedName("StartTime")
    var startTime: String? = "",
    @SerializedName("StartLat")
    var startLat: String? = "",
    @SerializedName("StartLong")
    var startLong: String? = "",
    @SerializedName("StartAddress")
    var startAddress: String? = "",
    @SerializedName("EndTime")
    var endTime: String? = "",
    @SerializedName("EndLat")
    var endLat: String? = "",
    @SerializedName("EndLong")
    var endLong: String? = "",
    @SerializedName("EndAddress")
    var endAddress: String? = "",
    @SerializedName("Date")
    var date: String? = "",
    @SerializedName("Work")
    var work: String? = "",
    @SerializedName("App_Version")
    var appVersion:String? = "",
    @SerializedName("Department")
    var department:String? = "",
    @SerializedName("TotalTime")
    var totalTime: Int = 0
)