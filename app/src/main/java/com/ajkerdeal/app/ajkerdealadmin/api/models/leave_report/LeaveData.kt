package com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeaveData(
    @SerializedName("session")
    var session: List<LeaveDataDetails>? = listOf(),
    @SerializedName("Name")
    var name: String? = "",
    @SerializedName("UserId")
    var userId: String? = "",
    @SerializedName("Sick_Leave")
    var sickLeave: Int = 0,
    @SerializedName("Casual_Leave")
    var casualLeave: Int = 0
): Parcelable