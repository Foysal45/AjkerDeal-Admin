package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class ComplainUpdateRequest(
    @SerializedName("BookingCode")
    val bookingCode: String = "",
    @SerializedName("Comments")
    val comments: String = "",
    @SerializedName("ComplainType")
    val complainType: String = "",
    @SerializedName("IsIssueSolved")
    val isIssueSolved: String = "",
    @SerializedName("UpdatedBy ")
    val updatedBy: Int = 0
)