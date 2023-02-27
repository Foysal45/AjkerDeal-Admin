package com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session


import com.google.gson.annotations.SerializedName

data class EmployeeSessionStartStopRequest(
    @SerializedName("Userid")
    var userid: String = "",
    @SerializedName("UserName")
    var userName: String = "",
    @SerializedName("From")
    var from: String = "",
    @SerializedName("IsPresent")
    var isPresent: Boolean = false,
    @SerializedName("StartLat")
    var startLat: String = "",
    @SerializedName("StartLong")
    var startLong: String = "",
    @SerializedName("StartAddress")
    var startAddress: String = "",
    @SerializedName("Work")
    var work: String = "",
    @SerializedName("EndLat")
    var endLat: String = "",
    @SerializedName("EndLong")
    var endLong: String = "",
    @SerializedName("EndAddress")
    var endAddress: String = "",
    @SerializedName("Department")
    var department: String = "",
    @SerializedName("App_Version")
    var app_Version: String = ""
)