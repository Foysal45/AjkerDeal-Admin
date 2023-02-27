package com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report


import com.google.gson.annotations.SerializedName

data class AutoAssignedCountDetailsRequest(
    @SerializedName("AssignedTo")
    var assignedTo: Int = 0,
    @SerializedName("Flag")
    var flag: Int = 0,
    @SerializedName("FromDate")
    var fromDate: String = "",
    @SerializedName("IsIssueSolved")
    var isIssueSolved: Int = 0,
    @SerializedName("ToDate")
    var toDate: String = "",
    @SerializedName("UpdatedBy")
    var updatedBy: Int = 0
)