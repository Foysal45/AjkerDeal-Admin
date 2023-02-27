package com.ajkerdeal.app.ajkerdealadmin.api.models.break_model

import com.google.gson.annotations.SerializedName


data class BreakResponse(
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("status")
    var status: Boolean = false
)