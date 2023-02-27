package com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report


import com.google.gson.annotations.SerializedName

data class GetLoanSurveyRequestBody(
    @SerializedName("fromDate")
    var fromDate: String = "",
    @SerializedName("toDate")
    var toDate: String = ""
)