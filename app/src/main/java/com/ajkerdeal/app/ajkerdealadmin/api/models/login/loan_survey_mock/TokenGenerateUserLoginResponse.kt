package com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock


import com.google.gson.annotations.SerializedName

data class TokenGenerateUserLoginResponse(
    @SerializedName("acquisitionUserId")
    var acquisitionUserId: Int = 0,
    @SerializedName("actionModel")
    var actionModel: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("adminUsers")
    var adminUsers: AdminUsers = AdminUsers(),
    @SerializedName("advancePayment")
    var advancePayment: Double = 0.0,
    @SerializedName("alterMobile")
    var alterMobile: String = "",
    @SerializedName("areaId")
    var areaId: Int = 0,
    @SerializedName("areaName")
    var areaName: String = "",
    @SerializedName("bkashNumber")
    var bkashNumber: String = "",
    @SerializedName("blockReason")
    var blockReason: String = "",
    @SerializedName("categoryId")
    var categoryId: Int = 0,
    @SerializedName("collectionCharge")
    var collectionCharge: Double = 0.0,
    @SerializedName("companyName")
    var companyName: String = "",
    @SerializedName("courierOrders")
    var courierOrders: String = "",
    @SerializedName("courierUserId")
    var courierUserId: Int = 0,
    @SerializedName("credit")
    var credit: Double = 0.0,
    @SerializedName("customerSMSLimit")
    var customerSMSLimit: Int = 0,
    @SerializedName("customerVoiceSmsLimit")
    var customerVoiceSmsLimit: String = "",
    @SerializedName("deliveryRange")
    var deliveryRange: String = "",
    @SerializedName("deliveryRangeIdIOutside")
    var deliveryRangeIdIOutside: Int = 0,
    @SerializedName("deliveryRangeIdInside")
    var deliveryRangeIdInside: Int = 0,
    @SerializedName("districtId")
    var districtId: Int = 0,
    @SerializedName("districtName")
    var districtName: String = "",
    @SerializedName("districtsViewModel")
    var districtsViewModel: DistrictsViewModel = DistrictsViewModel(),
    @SerializedName("email")
    var emailOfUser: Boolean = false,
    @SerializedName("emailAddress")
    var emailAddress: String = "",
    @SerializedName("fburl")
    var fburl: String = "",
    @SerializedName("firebaseToken")
    var firebaseToken: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("isActive")
    var isActive: Boolean = false,
    @SerializedName("isAutoProcess")
    var isAutoProcess: Boolean = false,
    @SerializedName("isBlock")
    var isBlock: Boolean = false,
    @SerializedName("isBreakAble")
    var isBreakAble: String = "",
    @SerializedName("isCustomerEmail")
    var isCustomerEmail: Boolean = false,
    @SerializedName("isCustomerSms")
    var isCustomerSms: Boolean = false,
    @SerializedName("isDocument")
    var isDocument: Boolean = false,
    @SerializedName("isEmail")
    var isEmail: Boolean = false,
    @SerializedName("isHeavyWeight")
    var isHeavyWeight: String = "",
    @SerializedName("isLoanActive")
    var isLoanActive: Boolean = false,
    @SerializedName("isOfferActive")
    var isOfferActive: Boolean = false,
    @SerializedName("isQuickOrderActive")
    var isQuickOrderActive: String = "",
    @SerializedName("isSms")
    var hasSms: Boolean = false,
    @SerializedName("joinDate")
    var joinDate: String = "",
    @SerializedName("knowingSource")
    var knowingSource: String = "",
    @SerializedName("loanCompany")
    var loanCompany: String = "",
    @SerializedName("mailCharge")
    var mailCharge: Double = 0.0,
    @SerializedName("maxCodCharge")
    var maxCodCharge: Double = 0.0,
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("offerBkashDiscountDhaka")
    var offerBkashDiscountDhaka: Double = 0.0,
    @SerializedName("offerBkashDiscountOutSideDhaka")
    var offerBkashDiscountOutSideDhaka: Double = 0.0,
    @SerializedName("offerCodDiscount")
    var offerCodDiscount: Double = 0.0,
    @SerializedName("offerType")
    var offerType: Int = 0,
    @SerializedName("orderRequestList")
    var orderRequestList: String = "",
    @SerializedName("orderType")
    var orderType: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("pickupLocationList")
    var pickupLocationList: String = "",
    @SerializedName("preferredPaymentCycle")
    var preferredPaymentCycle: String = "",
    @SerializedName("preferredPaymentCycleDate")
    var preferredPaymentCycleDate: String = "",
    @SerializedName("priority")
    var priority: String = "",
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
    @SerializedName("refreshToken")
    var refreshToken: String = "",
    @SerializedName("refreshtoken")
    var refreshtoken: String = "",
    @SerializedName("registrationFrom")
    var registrationFrom: String = "",
    @SerializedName("remarks")
    var remarks: String = "",
    @SerializedName("retentionMerchantOrder")
    var retentionMerchantOrder: String = "",
    @SerializedName("retentionUserId")
    var retentionUserId: Int = 0,
    @SerializedName("returnCharge")
    var returnCharge: Double = 0.0,
    @SerializedName("sms")
    var sms: Boolean = false,
    @SerializedName("smsCharge")
    var smsCharge: Double = 0.0,
    @SerializedName("sourceType")
    var sourceType: String = "",
    @SerializedName("statusId")
    var statusId: List<Int> = listOf(),
    @SerializedName("subCategoryId")
    var subCategoryId: Int = 0,
    @SerializedName("teleSalesDate")
    var teleSalesDate: String = "",
    @SerializedName("thanaId")
    var thanaId: Int = 0,
    @SerializedName("thanaName")
    var thanaName: String = "",
    @SerializedName("token")
    var token: String = "",
    @SerializedName("userName")
    var userName: String = "",
    @SerializedName("vouchersViewModel")
    var vouchersViewModel: String = "",
    @SerializedName("webURL")
    var webURL: String = "",
    @SerializedName("weightRangeId")
    var weightRangeId: Int = 0
)