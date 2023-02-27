package com.ajkerdeal.app.ajkerdealadmin.api.models.track_order


import com.google.gson.annotations.SerializedName

data class DistrictsModel(
    @SerializedName("area")
    var area: String? = "",
    @SerializedName("district")
    var district: String? = "",
    @SerializedName("districtId")
    var districtId: Int = 0,
    @SerializedName("edeshMobileNo")
    var edeshMobileNo: String? = "",
    @SerializedName("thana")
    var thana: String? = "",
    @SerializedName("thanaId")
    var thanaId: Int = 0,
    @SerializedName("tigerMobileNo")
    var tigerMobileNo: String? = ""
)