package com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session


import com.google.gson.annotations.SerializedName

data class EmployeeWorkTimeResponse(
    @SerializedName("StartTime")
    var startTime:String? = "",
    @SerializedName("EndTime")
    var endTime:String? = "",
    @SerializedName("breaktime")
    var breakTime:String? = "",
    @SerializedName("workingtime")
    var workingTime: String? = ""
)