package com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session


import com.google.gson.annotations.SerializedName

data class LastDaySessionStopRequest(
    @SerializedName("UserId")
    var userId: String = "",
    @SerializedName("Time")
    var time: String = "",
    @SerializedName("Work")
    var work: String = ""
)