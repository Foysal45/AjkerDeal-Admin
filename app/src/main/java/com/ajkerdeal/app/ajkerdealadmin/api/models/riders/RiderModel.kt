package com.ajkerdeal.app.ajkerdealadmin.api.models.riders


import com.google.gson.annotations.SerializedName

data class RiderModel(
    @SerializedName("firebaseToken")
    val firebaseToken: String = "",
    @SerializedName("hubName")
    val hubName: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("isActive")
    val isActive: Int = 0,
    @SerializedName("isDrivingLicense")
    val isDrivingLicense: Boolean = false,
    @SerializedName("isNID")
    val isNID: Boolean = false,
    @SerializedName("isNowOffline")
    val isNowOffline: Boolean = false,
    @SerializedName("isPermanentRider")
    val isPermanentRider: Boolean = false,
    @SerializedName("isProfileImage")
    val isProfileImage: Boolean = false,
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("riderType")
    val riderType: String = "",
    @SerializedName("updatedBy")
    val updatedBy: Int = 0,
    @SerializedName("updatedOn")
    val updatedOn: String = "",
    @SerializedName("userType")
    val userType: Int = 0
)