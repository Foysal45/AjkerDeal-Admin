package com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CouriersWithLoanSurveyViewModel(
    @SerializedName("courierId")
    var courierId: Int = 0,
    @SerializedName("courierName")
    var courierName: String = "",
    @SerializedName("couriersWithLoanSurveyId")
    var couriersWithLoanSurveyId: Int = 0,
    @SerializedName("loanSurveyId")
    var loanSurveyId: Int = 0
) : Parcelable