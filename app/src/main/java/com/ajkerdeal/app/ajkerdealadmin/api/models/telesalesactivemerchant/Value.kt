package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class Value(
    @SerializedName("teleSales")
    var teleSales: String = "",
    @SerializedName("totalCount")
    var totalCount: Int = 0
)