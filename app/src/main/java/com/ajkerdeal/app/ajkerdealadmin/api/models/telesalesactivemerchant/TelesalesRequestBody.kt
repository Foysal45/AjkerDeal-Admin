package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TelesalesRequestBody(
    @SerializedName("fromDate")
    var fromDate: String = "",
    @SerializedName("toDate")
    var toDate: String = ""
)