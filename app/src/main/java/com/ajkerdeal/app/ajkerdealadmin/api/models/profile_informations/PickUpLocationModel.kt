package com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations


import com.google.gson.annotations.SerializedName

data class PickUpLocationModel(
    @SerializedName("courierUserId")
    val courierUserId: Int = 0,
    @SerializedName("districtId")
    val districtId: Int = 0,
    @SerializedName("districtName")
    val districtName: String = "",
    @SerializedName("districtNameEng")
    val districtNameEng: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("pickupAddress")
    var pickupAddress: String = "",
    @SerializedName("thanaId")
    val thanaId: Int = 0,
    @SerializedName("thanaName")
    val thanaName: String = "",
    @SerializedName("thanaNameEng")
    val thanaNameEng: String = ""
)