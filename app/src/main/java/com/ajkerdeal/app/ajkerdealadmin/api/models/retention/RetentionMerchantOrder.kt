package com.ajkerdeal.app.ajkerdealadmin.api.models.retention


import com.google.gson.annotations.SerializedName

data class RetentionMerchantOrder(
    @SerializedName("lastOrderDate")
    var lastOrderDate: String? = "",
    @SerializedName("totalOrder")
    var totalOrder: Int = 0
)