package com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order


import com.google.gson.annotations.SerializedName

data class TimeSlotRequest(
    @SerializedName("requestDate")
    var requestDate: String? = ""
)