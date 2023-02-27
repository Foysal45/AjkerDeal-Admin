package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders


import com.google.gson.annotations.SerializedName

data class CourierOrderInfo(
    @SerializedName("paymentType")
    var paymentType: String? = null,
    @SerializedName("orderType")
    var orderType: String? = null,
    @SerializedName("weight")
    var weight: String? = null,
    @SerializedName("weightRangeId")
    var weightRangeId: Int = 0,
    @SerializedName("collectionName")
    var collectionName: String? = null
)