package com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session


import com.google.gson.annotations.SerializedName

data class EmployeeSessionResponse(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("msg")
    var msg: String = ""
)