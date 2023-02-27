package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TeleSalesActiveMerchantResponse(
    @SerializedName("day")
    var day: Int = 0,
    @SerializedName("month")
    var month: Int = 0,
    @SerializedName("teleSalesDate")
    var teleSalesDate: String = "",
    @SerializedName("values")
    var values: List<Value> = listOf(),
    @SerializedName("year")
    var year: Int = 0
)