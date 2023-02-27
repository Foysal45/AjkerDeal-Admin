package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders


import com.google.gson.annotations.SerializedName

data class CourierOrderDateDetails(
    @SerializedName("updatedOnDate")
    var updatedOnDate: String? = null,
    @SerializedName("confirmationDate")
    var confirmationDate: String? = null,
    @SerializedName("orderDate")
    var orderDate: String? = null,
    @SerializedName("postedOn")
    var postedOn: String? = null,
    @SerializedName("logPostedOn")
    var logPostedOn: String? = null
)