package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class ComplainListRequest(
    @SerializedName("FromDate")
    var fromDate: String = "",
    @SerializedName("ToDate")
    var toDate: String = "",
    @SerializedName("AnswerBy")
    var answerBy: Int = 0,
    @SerializedName("IsFilterByUserId")
    var isFilterByUserId: Int = 0,
    @SerializedName("IsIssueSolved")
    var isIssueSolved: String = ""
)