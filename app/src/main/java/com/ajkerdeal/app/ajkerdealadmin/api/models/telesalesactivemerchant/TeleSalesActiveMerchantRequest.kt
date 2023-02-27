package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TeleSalesActiveMerchantRequest(
    @SerializedName("fromDate")
    val fromDate: String = "",
    @SerializedName("retentionUserId")
    val retentionUserId: Int = 0,
    @SerializedName("statusId")
    val statusId: Int = 0
)