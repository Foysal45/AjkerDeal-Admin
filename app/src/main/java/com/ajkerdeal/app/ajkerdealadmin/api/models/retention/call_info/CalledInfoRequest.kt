package com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info

import com.google.gson.annotations.SerializedName

data class CalledInfoRequest(
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("adminUserId")
    var adminUserId: Int = 0,
    @SerializedName("mobile")
    var mobile: String? = "",
    @SerializedName("callDuration")
    var callDuration: Int = 0,
    @SerializedName("calledSummary")
    var calledSummary: String? = ""
)