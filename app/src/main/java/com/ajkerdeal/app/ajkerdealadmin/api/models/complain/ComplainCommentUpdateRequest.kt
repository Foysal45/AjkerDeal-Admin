package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class ComplainCommentUpdateRequest(
    @SerializedName("BookingCode")
    var bookingCode: String? = "",
    @SerializedName("Comments")
    var comments: String? = "",
    @SerializedName("AnswerBy")
    var answerBy: Int = 0
)