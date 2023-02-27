package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class MerchantComplainData(
    @SerializedName("SolvedDate")
    var solvedDate: String? = "",
    @SerializedName("ComplaintDate")
    var complaintDate: String? = "",
    @SerializedName("AnswerBy")
    var answerBy: Int = 0,
    @SerializedName("OrderId")
    var orderId: Int = 0,
    @SerializedName("CurrentStatus")
    var currentStatus: String? = "",
    @SerializedName("FullName")
    var fullName: String? = "",
    @SerializedName("Comments")
    var comments: String? = "",
    @SerializedName("ComplainType")
    var complainType: String? = "",
    @SerializedName("AssignedTo")
    var assignedTo: String? = "",

    //Internal use only
    var isExpand: Boolean = false
)