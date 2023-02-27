package com.ajkerdeal.app.ajkerdealadmin.api.models


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("Message")
    var message: String? = ""
)