package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("courierUserId")
    var courierUserId: Int? = null,
    @SerializedName("companyName")
    var companyName: String? = null,
    @SerializedName("userName")
    var userName: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("emailAddress")
    var emailAddress: String? = null,
    @SerializedName("collectAddress")
    var collectAddress: String? = null,
    @SerializedName("bkashNumber")
    var bkashNumber: String? = null
)