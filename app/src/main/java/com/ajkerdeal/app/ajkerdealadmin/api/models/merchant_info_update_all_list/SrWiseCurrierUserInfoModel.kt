package com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list


import com.google.gson.annotations.SerializedName

data class SrWiseCurrierUserInfoModel(
    @SerializedName("courierUsers")
    val courierUsers: List<CourierUser>,
    @SerializedName("totalUsers")
    val totalUsers: Int
)