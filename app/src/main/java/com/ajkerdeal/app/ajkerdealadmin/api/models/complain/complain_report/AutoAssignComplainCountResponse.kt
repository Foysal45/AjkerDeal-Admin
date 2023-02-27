package com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AutoAssignComplainCountResponse(
    @SerializedName("AnswerBy")
    var answerBy: Int = 0,
    @SerializedName("AssignedTo")
    var assignedTo: Int = 0,
    @SerializedName("BookingCode")
    var bookingCode: String = "",
    @SerializedName("FullName")
    var fullName: String = "",
    @SerializedName("PendingComplain")
    var pendingComplain: Int = 0,
    @SerializedName("SolvedComplain")
    var solvedComplain: Int = 0,
    @SerializedName("TotalComplain")
    var totalComplain: Int = 0,
    @SerializedName("TouchedComplain")
    var touchedComplain: Int = 0,
    @SerializedName("UntouchedComplain")
    var untouchedComplain: Int = 0
): Parcelable