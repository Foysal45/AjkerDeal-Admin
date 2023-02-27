package com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report


import com.google.gson.annotations.SerializedName

data class AutoAssignComplainCountRequest(
    @SerializedName("FromDate")
    var fromDate: String = "",
    @SerializedName("ToDate")
    var toDate: String = ""
)