package com.ajkerdeal.app.ajkerdealadmin.api.models

import com.google.gson.annotations.SerializedName

data class GenericResponse<T>(
    @SerializedName("message")
    var message: String,
    @SerializedName("didError")
    var didError: Boolean,
    @SerializedName("errorMessage")
    var errorMessage: String,
    @SerializedName("model")
    var model: T
)