package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders


import com.google.gson.annotations.SerializedName

data class CODResponse(
    @SerializedName("totalCount")
    var totalCount: Double = 0.0,
    @SerializedName("adTotalCollectionAmount")
    var adTotalCollectionAmount: Double = 0.0,
    @SerializedName("adCourierPaymentInfo")
    var adCourierPaymentInfo: AdCourierPaymentInfo? = AdCourierPaymentInfo(),
    @SerializedName("courierOrderViewModel")
    var courierOrderViewModel: List<CourierOrderViewModel> = listOf()
)