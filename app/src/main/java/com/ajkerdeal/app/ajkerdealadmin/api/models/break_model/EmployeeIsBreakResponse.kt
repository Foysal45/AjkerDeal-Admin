package com.ajkerdeal.app.ajkerdealadmin.api.models.break_model

import com.google.gson.annotations.SerializedName


data class EmployeeIsBreakResponse(
    @SerializedName("status")
    var status: Boolean = false
)