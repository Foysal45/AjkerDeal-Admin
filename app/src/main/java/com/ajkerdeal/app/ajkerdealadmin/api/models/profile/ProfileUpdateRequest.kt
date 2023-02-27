package com.ajkerdeal.app.ajkerdealadmin.api.models.profile
import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("UserId")
    var userId: String? = "",
    @SerializedName("FullName")
    var fullName: String? = "",
    @SerializedName("Gender")
    var gender: String? = "",
    @SerializedName("Address")
    var address: String? = "",
    @SerializedName("Mobile")
    var mobile: String? = "",
    @SerializedName("BloodGroup")
    var bloodGroup: String? = "",
    @SerializedName("PersonalEmail")
    var personalEmail: String? = ""
)