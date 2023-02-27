package com.ajkerdeal.app.ajkerdealadmin.api.models.complain


import com.google.gson.annotations.SerializedName

data class ComplainRequest(
    @SerializedName("OrderId")
    var orderId: String? = "",
    @SerializedName("Comments")
    var comments: String? = "",
    @SerializedName("ComplainFrom")
    var orderFrom: String = "adadminapp",
    @SerializedName("MerchantName")
    var merchantName: String = "",
    @SerializedName("Mobile")
    var mobile: String = "",
    @SerializedName("AnswerBy")
    var answerBy: Int? = 0,


)