package com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session


import com.google.gson.annotations.SerializedName

data class EmployeeStatusResponse(
    @SerializedName("IsPresent")
    var IsPresent: Boolean = false,
    @SerializedName("status")
    var status: Boolean = false
)