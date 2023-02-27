package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list


import com.google.gson.annotations.SerializedName

data class MerchantUpdateRequest(
    @SerializedName("count")
    val count: Int,
    @SerializedName("index")
    val index: Int,
    @SerializedName("retentionUserId")
    val retentionUserId: Int,
    @SerializedName("search")
    val search: String
)