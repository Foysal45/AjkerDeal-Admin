package com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock


import com.google.gson.annotations.SerializedName

data class DistrictsViewModel(
    @SerializedName("area")
    var area: Any = Any(),
    @SerializedName("district")
    var district: Any = Any(),
    @SerializedName("districtId")
    var districtId: Int = 0,
    @SerializedName("edeshMobileNo")
    var edeshMobileNo: Any = Any(),
    @SerializedName("thana")
    var thana: Any = Any(),
    @SerializedName("thanaId")
    var thanaId: Int = 0,
    @SerializedName("tigerMobileNo")
    var tigerMobileNo: Any = Any()
)