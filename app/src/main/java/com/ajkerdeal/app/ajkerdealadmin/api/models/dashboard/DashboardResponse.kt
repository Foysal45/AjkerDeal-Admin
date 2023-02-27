package com.ajkerdeal.app.ajkerdealadmin.api.models.dashboard


import com.ajkerdeal.app.ajkerdealadmin.api.models.dashboard.DashboardData
import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("pickDashboardViewModel")
    var pickDashboardViewModel: List<DashboardData>? = listOf(),
    @SerializedName("orderDashboardViewModel")
    var orderDashboardViewModel: List<DashboardData>? = listOf(),
    @SerializedName("paymentDashboardViewModel")
    var paymentDashboardViewModel: List<DashboardData>? = listOf()
)