package com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations


import com.google.gson.annotations.SerializedName

data class DistrictModel(
    @SerializedName("areaType")
    val areaType: Int = 0,
    @SerializedName("district")
    val district: String = "",
    @SerializedName("districtBng")
    val districtBng: String = "",
    @SerializedName("districtId")
    val districtId: Int = 0,
    @SerializedName("districtPriority")
    val districtPriority: Int = 0,
    @SerializedName("eDeshMobileNo")
    val eDeshMobileNo: String = "",
    @SerializedName("hasExpressDelivery")
    val hasExpressDelivery: Int = 0,
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("isActiveForCorona")
    val isActiveForCorona: Boolean = false,
    @SerializedName("isCity")
    val isCity: Boolean = false,
    @SerializedName("isDtOwnSecondMileDelivery")
    val isDtOwnSecondMileDelivery: Boolean = false,
    @SerializedName("isPickupLocation")
    val isPickupLocation: Boolean = false,
    @SerializedName("ownSecondMileDelivery")
    val ownSecondMileDelivery: String = "",
    @SerializedName("paperflyAreaName")
    val paperflyAreaName: String = "",
    @SerializedName("parentId")
    val parentId: Int = 0,
    @SerializedName("postalCode")
    val postalCode: String = "",
    @SerializedName("redxAreaId")
    val redxAreaId: Int = 0,
    @SerializedName("redxAreaName")
    val redxAreaName: String = "",
    @SerializedName("redxHubName")
    val redxHubName: String = "",
    @SerializedName("tigerMobileNo")
    val tigerMobileNo: String = "",
    @SerializedName("updatedBy")
    val updatedBy: Int = 0,
    @SerializedName("updatedOn")
    val updatedOn: String = "",

    //inner variable
    var searchKey: String = "", // lower case
)