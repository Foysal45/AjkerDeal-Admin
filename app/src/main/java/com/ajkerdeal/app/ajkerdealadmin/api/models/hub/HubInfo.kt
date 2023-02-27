package com.ajkerdeal.app.ajkerdealadmin.api.models.hub


import com.google.gson.annotations.SerializedName

data class HubInfo(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("value")
    var value: String? = "",
    @SerializedName("isActive")
    var isActive: Boolean = false,
    @SerializedName("hubAddress")
    var hubAddress: String? = "",
    @SerializedName("longitude")
    var longitude: String? = "",
    @SerializedName("latitude")
    var latitude: String? = "",
    @SerializedName("hubMobile")
    var hubMobile: String? = ""
)