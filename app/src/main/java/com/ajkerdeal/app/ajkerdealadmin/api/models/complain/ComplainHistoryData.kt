package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class ComplainHistoryData(
    @SerializedName("BookingCode")
    var bookingCode: Int = 0,
    @SerializedName("CommentedBy")
    var commentedBy: String? = "",
    @SerializedName("MerchantName")
    var merchantName: String? = "",
    @SerializedName("Comments")
    var comments: String? = "",
    @SerializedName("UserName")
    var userName: String? = "",
    @SerializedName("UpdatedOn")
    var updatedOn: String? = ""
)