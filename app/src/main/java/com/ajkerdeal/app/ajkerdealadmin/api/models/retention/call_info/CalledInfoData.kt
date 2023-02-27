package com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info

import com.google.gson.annotations.SerializedName

data class CalledInfoData(
    @SerializedName("merchantCalledId")
    var merchantCalledId: Int = 0,
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("adminUserId")
    var adminUserId: Int = 0,
    @SerializedName("mobile")
    var mobile: String? = "",
    @SerializedName("callDuration")
    var callDuration: Double = 0.0,
    @SerializedName("calledSummary")
    var calledSummary: String? = "",
    @SerializedName("calledDate")
    var calledDate: String? = ""
)