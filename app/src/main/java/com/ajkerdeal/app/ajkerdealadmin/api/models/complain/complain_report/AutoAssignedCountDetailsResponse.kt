package com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report


import com.google.gson.annotations.SerializedName

data class AutoAssignedCountDetailsResponse(
    @SerializedName("BookingCode")
    var bookingCode: String = "",
    @SerializedName("ComplainType")
    var complainType: String = "",
    @SerializedName("CourierName")
    var courierName: String = "",
    @SerializedName("DateOfCall")
    var dateOfCall: String = "",
    @SerializedName("FullName")
    var fullName: String = "",
    @SerializedName("IsIssueSolved")
    var isIssueSolved: Int = 0,
    @SerializedName("OrderDate")
    var orderDate: String = "",
    @SerializedName("PodNumber")
    var podNumber: String = "",
    @SerializedName("PostedOn")
    var postedOn: Any = Any(),
    @SerializedName("UpdatedOn")
    var updatedOn: String = ""
)