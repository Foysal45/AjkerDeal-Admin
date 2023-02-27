package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count


import com.google.gson.annotations.SerializedName

data class OrderCountReqBody(
    @SerializedName("month")
    var month: Int = 0,
    @SerializedName("year")
    var year: Int = 0,
    @SerializedName("fromDate")
    var fromDate: String = "",
    @SerializedName("toDate")
    var toDate: String = "",
    @SerializedName("courierUserId")
    var courierUserId: Int = 0
)