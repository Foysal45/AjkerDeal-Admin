package com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report


import com.google.gson.annotations.SerializedName

data class GetCourrierUsersInfoResponse(
    @SerializedName("accountName")
    var accountName: String = "",
    @SerializedName("accountNumber")
    var accountNumber: String = "",
    @SerializedName("acquisitionUserId")
    var acquisitionUserId: Int = 0,
    @SerializedName("address")
    var address: String = "",
    @SerializedName("advancePayment")
    var advancePayment: Int = 0,
    @SerializedName("alterMobile")
    var alterMobile: String = "",
    @SerializedName("bankName")
    var bankName: String = "",
    @SerializedName("bkashNumber")
    var bkashNumber: String = "",
    @SerializedName("blockReason")
    var blockReason: String = "",
    @SerializedName("branchName")
    var branchName: String = "",
    @SerializedName("categoryId")
    var categoryId: Int = 0,
    @SerializedName("categoryName")
    var categoryName: String = "",
    @SerializedName("collectionCharge")
    var collectionCharge: Int = 0,
    @SerializedName("companyName")
    var companyName: String = "",
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("credit")
    var credit: Int = 0,
    @SerializedName("customerSMSLimit")
    var customerSMSLimit: Int = 0,
    @SerializedName("customerVoiceSmsLimit")
    var customerVoiceSmsLimit: Int = 0,
    @SerializedName("deliveryRangeIdIOutside")
    var deliveryRangeIdIOutside: Int = 0,
    @SerializedName("deliveryRangeIdInside")
    var deliveryRangeIdInside: Int = 0,
    @SerializedName("districtId")
    var districtId: Int = 0,
    @SerializedName("emailAddress")
    var emailAddress: String = "",
    @SerializedName("fburl")
    var fburl: String = "",
    @SerializedName("firebaseToken")
    var firebaseToken: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("hashPassword")
    var hashPassword: String = "",
    @SerializedName("isActive")
    var isActive: Boolean = false,
    @SerializedName("isAutoProcess")
    var isAutoProcess: Boolean = false,
    @SerializedName("isBlock")
    var isBlock: Boolean = false,
    @SerializedName("isBreakAble")
    var isBreakAble: Boolean = false,
    @SerializedName("isCustomerEmail")
    var isCustomerEmail: Boolean = false,
    @SerializedName("isCustomerSms")
    var isCustomerSms: Boolean = false,
    @SerializedName("isDocument")
    var isDocument: Boolean = false,
    @SerializedName("isEmail")
    var isEmail: Boolean = false,
    @SerializedName("isHeavyWeight")
    var isHeavyWeight: Boolean = false,
    @SerializedName("isLoanActive")
    var isLoanActive: Boolean = false,
    @SerializedName("isOfferActive")
    var isOfferActive: Boolean = false,
    @SerializedName("isQuickOrderActive")
    var isQuickOrderActive: Boolean = false,
    @SerializedName("isSms")
    var isSms: Boolean = false,
    @SerializedName("isTeleSales")
    var isTeleSales: Boolean = false,
    @SerializedName("joinDate")
    var joinDate: String = "",
    @SerializedName("knowingSource")
    var knowingSource: String = "",
    @SerializedName("loanCompany")
    var loanCompany: String = "",
    @SerializedName("mailCharge")
    var mailCharge: Int = 0,
    @SerializedName("maxCodCharge")
    var maxCodCharge: Int = 0,
    @SerializedName("merchantAssignActive")
    var merchantAssignActive: Boolean = false,
    @SerializedName("merchantReview")
    var merchantReview: Int = 0,
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("offerBkashDiscountDhaka")
    var offerBkashDiscountDhaka: Int = 0,
    @SerializedName("offerBkashDiscountOutSideDhaka")
    var offerBkashDiscountOutSideDhaka: Int = 0,
    @SerializedName("offerCodDiscount")
    var offerCodDiscount: Int = 0,
    @SerializedName("offerType")
    var offerType: Int = 0,
    @SerializedName("orderType")
    var orderType: String = "",
    @SerializedName("paymentServiceCharge")
    var paymentServiceCharge: Int = 0,
    @SerializedName("paymentServiceType")
    var paymentServiceType: Int = 0,
    @SerializedName("preferredPaymentCycle")
    var preferredPaymentCycle: String = "",
    @SerializedName("preferredPaymentCycleDate")
    var preferredPaymentCycleDate: String = "",
    @SerializedName("priority")
    var priority: String = "",
    @SerializedName("recommendation")
    var recommendation: Int = 0,
    @SerializedName("refereeEndTime")
    var refereeEndTime: String = "",
    @SerializedName("refereeOrder")
    var refereeOrder: Int = 0,
    @SerializedName("refereeStartTime")
    var refereeStartTime: String = "",
    @SerializedName("referrer")
    var referrer: String = "",
    @SerializedName("referrerEndTime")
    var referrerEndTime: String = "",
    @SerializedName("referrerIsActive")
    var referrerIsActive: Boolean = false,
    @SerializedName("referrerOrder")
    var referrerOrder: Int = 0,
    @SerializedName("referrerStartTime")
    var referrerStartTime: String = "",
    @SerializedName("refreshtoken")
    var refreshtoken: String = "",
    @SerializedName("registrationFrom")
    var registrationFrom: String = "",
    @SerializedName("remarks")
    var remarks: String = "",
    @SerializedName("retentionUserId")
    var retentionUserId: Int = 0,
    @SerializedName("returnCharge")
    var returnCharge: Int = 0,
    @SerializedName("routingNumber")
    var routingNumber: String = "",
    @SerializedName("smsCharge")
    var smsCharge: Int = 0,
    @SerializedName("sourceType")
    var sourceType: String = "",
    @SerializedName("subCategoryId")
    var subCategoryId: Int = 0,
    @SerializedName("subCategoryName")
    var subCategoryName: String = "",
    @SerializedName("teleSales")
    var teleSales: Int = 0,
    @SerializedName("teleSalesDate")
    var teleSalesDate: String = "",
    @SerializedName("thanaId")
    var thanaId: Int = 0,
    @SerializedName("userName")
    var userName: String = "",
    @SerializedName("webURL")
    var webURL: String = "",
    @SerializedName("weightRangeId")
    var weightRangeId: Int = 0
)