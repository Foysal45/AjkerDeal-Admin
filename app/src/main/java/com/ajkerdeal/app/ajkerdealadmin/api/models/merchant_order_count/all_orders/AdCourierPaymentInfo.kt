package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders


import com.google.gson.annotations.SerializedName

data class AdCourierPaymentInfo(
    @SerializedName("paymentInProcessing")
    var paymentInProcessing: Double? = null,
    @SerializedName("paymentPaid")
    var paymentPaid: Double? = null,
    @SerializedName("paymentReady")
    var paymentReady: Double? = null
)