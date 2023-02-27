package com.ajkerdeal.app.ajkerdealadmin.api.models.break_model

import com.google.gson.annotations.SerializedName


data class BreakRequest(
    @SerializedName("Userid")
    var userid: Int = 0,
    @SerializedName("reason")
    var reason: String = ""
)