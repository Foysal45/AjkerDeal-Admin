package com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info


import com.google.gson.annotations.SerializedName

data class CourierUsersInfoResponse(
    @SerializedName("courierUsers")
    var courierUsers: List<CourierUser>? = listOf(),
    @SerializedName("totalUsers")
    var totalUsers: Int = 0
)