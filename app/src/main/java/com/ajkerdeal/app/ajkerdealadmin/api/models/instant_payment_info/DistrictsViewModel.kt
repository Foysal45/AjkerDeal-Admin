package com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info


import com.google.gson.annotations.SerializedName

data class DistrictsViewModel(
    @SerializedName("area")
    val area: String = "",
    @SerializedName("district")
    val district: String = "",
    @SerializedName("districtId")
    val districtId: Int = 0,
    @SerializedName("edeshMobileNo")
    val edeshMobileNo: String = "",
    @SerializedName("thana")
    val thana: String = "",
    @SerializedName("thanaId")
    val thanaId: Int = 0,
    @SerializedName("tigerMobileNo")
    val tigerMobileNo: String = ""
)