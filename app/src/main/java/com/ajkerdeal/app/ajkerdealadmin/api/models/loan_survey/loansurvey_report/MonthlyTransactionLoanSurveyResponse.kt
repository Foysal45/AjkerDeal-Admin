package com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report


import com.google.gson.annotations.SerializedName

data class MonthlyTransactionLoanSurveyResponse(
    @SerializedName("month")
    var month: Int = 0,
    @SerializedName("monthName")
    var monthName: String = "",
    @SerializedName("totalCollectionAmount")
    var totalCollectionAmount: Int = 0,
    @SerializedName("year")
    var year: Int = 0
)