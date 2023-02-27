package com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info


import com.google.gson.annotations.SerializedName

data class VisitedInfoRequest(
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("adminUserId")
    var adminUserId: Int = 0,
    @SerializedName("latitude")
    var latitude: String? = "",
    @SerializedName("longitude")
    var longitude: String? = "",
    @SerializedName("visitedSummary")
    var visitedSummary: String? = ""
)