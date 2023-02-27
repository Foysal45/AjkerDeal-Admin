package com.ajkerdeal.app.ajkerdealadmin.api.models.dashboard


import com.google.gson.annotations.SerializedName

data class DashBoardReqBody(
    @SerializedName("month")
    var month: Int = 0,
    @SerializedName("year")
    var year: Int = 0,
    @SerializedName("fromDate")
    var fromDate: String = "",
    @SerializedName("toDate")
    var toDate: String = "",
    @SerializedName("courierUserId")
    var courierUserId: Int = 0
)