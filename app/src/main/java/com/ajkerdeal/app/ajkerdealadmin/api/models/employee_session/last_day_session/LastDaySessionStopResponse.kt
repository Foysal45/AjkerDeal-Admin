package com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session


import com.google.gson.annotations.SerializedName

data class LastDaySessionStopResponse(
    @SerializedName("status")
    var status: Boolean = false
)