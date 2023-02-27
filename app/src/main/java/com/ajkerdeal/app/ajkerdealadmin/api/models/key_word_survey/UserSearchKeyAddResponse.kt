package com.ajkerdeal.app.ajkerdealadmin.api.models.key_word_survey


import com.google.gson.annotations.SerializedName

data class UserSearchKeyAddResponse(
    @SerializedName("status")
    var status: Boolean = false,
    @SerializedName("msg")
    var msg: String? = ""
)