package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class MerchantComplainListRequest(
    @SerializedName("MerchantId")
    var merchantId: Int = 0,
    @SerializedName("Index")
    var index: Int = 0,
    @SerializedName("Count")
    var count: Int = 20
)