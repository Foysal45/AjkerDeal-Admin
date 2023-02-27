package com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info


import com.google.gson.annotations.SerializedName

data class InstantPymentRequest(
    @SerializedName("fromDate")
    val fromDate: String = "",
    @SerializedName("preferredPaymentCycle")
    val preferredPaymentCycle: String = "",
    @SerializedName("toDate")
    val toDate: String = ""
)