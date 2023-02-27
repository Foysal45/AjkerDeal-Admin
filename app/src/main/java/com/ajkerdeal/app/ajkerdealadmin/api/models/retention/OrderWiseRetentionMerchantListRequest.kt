package com.ajkerdeal.app.ajkerdealadmin.api.models.retention


import com.google.gson.annotations.SerializedName

data class OrderWiseRetentionMerchantListRequest(
    @SerializedName("retentionUserId")
    var retentionUserId: Int = 0,
    @SerializedName("index")
    var index: Int = 0,
    @SerializedName("count")
    var count: Int = 0,
    @SerializedName("search")
    var search: String? = "",
    @SerializedName("flag")
    var flag: Int = 0
)