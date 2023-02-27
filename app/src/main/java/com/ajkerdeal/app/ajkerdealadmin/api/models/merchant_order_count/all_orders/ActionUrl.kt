package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders


import com.google.gson.annotations.SerializedName

data class ActionUrl(
    @SerializedName("buttonName")
    var buttonName: String? = null,
    @SerializedName("url")
    var url: String? = null
)