package com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey


import com.google.gson.annotations.SerializedName

data class SelectedCourierModel(
    @SerializedName("courierId")
    var courierId: Int = 0,
    @SerializedName("courierName")
    var courierName: String = "",
    @SerializedName("loanSurveyId")
    var loanSurveyId: Int? = 0,
    @SerializedName("couriersWithLoanSurveyId")
    var couriersWithLoanSurveyId: Int? = 0
)