package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TeleSalesUpdateRequest(
    @SerializedName("teleSales")
    val teleSales: Int = 0,
    @SerializedName("teleSaleCourierUsers")
    val teleSalesCourierUsers: List<TelesalesCourierUsersModel> = listOf(),

)