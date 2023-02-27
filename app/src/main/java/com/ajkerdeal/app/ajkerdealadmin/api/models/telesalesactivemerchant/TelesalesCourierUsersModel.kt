package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TelesalesCourierUsersModel(
    @SerializedName("courierId")
    var courierId: Int = 0,
    @SerializedName("courierName")
    var courierName: String? = "",
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("teleSaleCourierUsersId")
    var teleSaleCourierUsersId: Int = 0,
    @SerializedName("teleSales")
    var teleSales: Int = 0
)