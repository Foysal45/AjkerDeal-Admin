package com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetloanSurveyResponseBody(
    @SerializedName("age")
    var age: String = "",
    @SerializedName("annualTotalIncome")
    var annualTotalIncome: Int = 0,
    @SerializedName("applicationDate")
    var applicationDate: String = "",
    @SerializedName("bankName")
    var bankName: String = "",
    @SerializedName("basketValue")
    var basketValue: String = "",
    @SerializedName("cardHolder")
    var cardHolder: String = "",
    @SerializedName("cardLimit")
    var cardLimit: String = "",
    @SerializedName("collectionAmountAvg")
    var collectionAmountAvg: Double = 0.0,
    @SerializedName("companyBankAccName")
    var companyBankAccName: String = "",
    @SerializedName("companyBankAccNo")
    var companyBankAccNo: String = "",
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("couriersWithLoanSurveyViewModel")
    var couriersWithLoanSurveyViewModel: List<CouriersWithLoanSurveyViewModel> = listOf(),
    @SerializedName("dateOfBirth")
    var dateOfBirth: String = "",
    @SerializedName("eduLevel")
    var eduLevel: String = "",
    @SerializedName("famMem")
    var famMem: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("guarantorMobile")
    var guarantorMobile: String = "",
    @SerializedName("guarantorName")
    var guarantorName: String = "",
    @SerializedName("hasCreditCard")
    var hasCreditCard: Boolean = false,
    @SerializedName("hasPreviousLoan")
    var hasPreviousLoan: Boolean = false,
    @SerializedName("hasTin")
    var hasTin: Boolean = false,
    @SerializedName("hasTradeLicense")
    var hasTradeLicense: Boolean = false,
    @SerializedName("homeOwnership")
    var homeOwnership: String = "",
    @SerializedName("interestedAmount")
    var interestedAmount: Int = 0,
    @SerializedName("isBankAccount")
    var isBankAccount: Boolean = false,
    @SerializedName("isLocalShop")
    var isLocalShop: Boolean = false,
    @SerializedName("lenderType")
    var lenderType: String = "",
    @SerializedName("loanAmount")
    var loanAmount: Int = 0,
    @SerializedName("loanEmi")
    var loanEmi: String = "",
    @SerializedName("married")
    var married: String = "",
    @SerializedName("merchantName")
    var merchantName: String = "",
    @SerializedName("monthlyExp")
    var monthlyExp: String = "",
    @SerializedName("monthlyOrder")
    var monthlyOrder: String = "",
    @SerializedName("monthlyTotalAverageSale")
    var monthlyTotalAverageSale: Int = 0,
    @SerializedName("monthlyTotalCodAmount")
    var monthlyTotalCodAmount: Int = 0,
    @SerializedName("nidNo")
    var nidNo: String = "",
    @SerializedName("othersIncome")
    var othersIncome: Int = 0,
    @SerializedName("recommend")
    var recommend: String = "",
    @SerializedName("relationMarchent")
    var relationMarchent: String = "",
    @SerializedName("repayType")
    var repayType: String = "",
    @SerializedName("reqTenorMonth")
    var reqTenorMonth: Int = 0,
    @SerializedName("residenceLocation")
    var residenceLocation: String = "",
    @SerializedName("shopOwnership")
    var shopOwnership: String = "",
    @SerializedName("tinNumber")
    var tinNumber: String = "",
    @SerializedName("tradeLicenseExpireDate")
    var tradeLicenseExpireDate: String = "",
    @SerializedName("tradeLicenseImageUrl")
    var tradeLicenseImageUrl: String = "",
    @SerializedName("tradeLicenseNo")
    var tradeLicenseNo: String = "",
    @SerializedName("transactionAmount")
    var transactionAmount: Int = 0
) : Parcelable