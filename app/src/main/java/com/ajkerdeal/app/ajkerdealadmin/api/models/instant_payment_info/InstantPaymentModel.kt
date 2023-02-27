package com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info


import com.google.gson.annotations.SerializedName

data class InstantPaymentModel(
    @SerializedName("acquisitionUserId")
    val acquisitionUserId: Int = 0,
    @SerializedName("actionModel")
    val actionModel: String= "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("adminUsers")
    val adminUsers: String= "",
    @SerializedName("advancePayment")
    val advancePayment: Double = 0.0,
    @SerializedName("alterMobile")
    val alterMobile: String= "",
    @SerializedName("areaId")
    val areaId: Int = 0,
    @SerializedName("areaName")
    val areaName: String = "",
    @SerializedName("bkashNumber")
    val bkashNumber: String = "",
    @SerializedName("blockReason")
    val blockReason: String= "",
    @SerializedName("categoryId")
    val categoryId: Int = 0,
    @SerializedName("collectionCharge")
    val collectionCharge: Double = 0.0,
    @SerializedName("companyName")
    val companyName: String = "",
    @SerializedName("courierOrders")
    val courierOrders: String= "",
    @SerializedName("courierUserId")
    val courierUserId: Int = 0,
    @SerializedName("credit")
    val credit: Double = 0.0,
    @SerializedName("customerSMSLimit")
    val customerSMSLimit: Int = 0,
    @SerializedName("customerVoiceSmsLimit")
    val customerVoiceSmsLimit: String= "",
    @SerializedName("deliveryRange")
    val deliveryRange: String= "",
    @SerializedName("deliveryRangeIdIOutside")
    val deliveryRangeIdIOutside: Int = 0,
    @SerializedName("deliveryRangeIdInside")
    val deliveryRangeIdInside: Int = 0,
    @SerializedName("districtId")
    val districtId: Int = 0,
    @SerializedName("districtName")
    val districtName: String = "",
    @SerializedName("districtsViewModel")
    val districtsViewModel: DistrictsViewModel = DistrictsViewModel(),
    @SerializedName("email")
    val email: Boolean = false,
    @SerializedName("emailAddress")
    val emailAddress: String = "",
    @SerializedName("fburl")
    val fburl: String = "",
    @SerializedName("firebaseToken")
    val firebaseToken: String = "",
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("isAutoProcess")
    val isAutoProcess: Boolean = false,
    @SerializedName("isBlock")
    val isBlock: Boolean = false,
    @SerializedName("isBreakAble")
    val isBreakAble: String= "",
    @SerializedName("isCustomerEmail")
    val isCustomerEmail: Boolean = false,
    @SerializedName("isCustomerSms")
    val isCustomerSms: Boolean = false,
    @SerializedName("isDocument")
    val isDocument: Boolean = false,
    @SerializedName("isEmail")
    val isEmail: Boolean = false,
    @SerializedName("isHeavyWeight")
    val isHeavyWeight: String= "",
    @SerializedName("isLoanActive")
    val isLoanActive: Boolean = false,
    @SerializedName("isOfferActive")
    val isOfferActive: Boolean = false,
    @SerializedName("isQuickOrderActive")
    val isQuickOrderActive: String= "",
    @SerializedName("isSms")
    val isSms: Boolean = false,
    @SerializedName("joinDate")
    val joinDate: String = "",
    @SerializedName("knowingSource")
    val knowingSource: String= "",
    @SerializedName("loanCompany")
    val loanCompany: String = "",
    @SerializedName("mailCharge")
    val mailCharge: Double = 0.0,
    @SerializedName("maxCodCharge")
    val maxCodCharge: Double = 0.0,
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("offerBkashDiscountDhaka")
    val offerBkashDiscountDhaka: Double = 0.0,
    @SerializedName("offerBkashDiscountOutSideDhaka")
    val offerBkashDiscountOutSideDhaka: Double = 0.0,
    @SerializedName("offerCodDiscount")
    val offerCodDiscount: Double = 0.0,
    @SerializedName("offerType")
    val offerType: Int = 0,
    @SerializedName("orderRequestList")
    val orderRequestList: String= "",
    @SerializedName("orderType")
    val orderType: String = "",
    @SerializedName("password")
    val password: String= "",
    @SerializedName("pickupLocationList")
    val pickupLocationList: String= "",
    @SerializedName("preferredPaymentCycle")
    val preferredPaymentCycle: String= "",
    @SerializedName("preferredPaymentCycleDate")
    val preferredPaymentCycleDate: String= "",
    @SerializedName("priority")
    val priority: String = "",
    @SerializedName("refereeEndTime")
    val refereeEndTime: String= "",
    @SerializedName("refereeOrder")
    val refereeOrder: Int = 0,
    @SerializedName("refereeStartTime")
    val refereeStartTime: String= "",
    @SerializedName("referrer")
    val referrer: String = "",
    @SerializedName("referrerEndTime")
    val referrerEndTime: String= "",
    @SerializedName("referrerIsActive")
    val referrerIsActive: Boolean = false,
    @SerializedName("referrerOrder")
    val referrerOrder: Int = 0,
    @SerializedName("referrerStartTime")
    val referrerStartTime: String= "",
    @SerializedName("refreshToken")
    val refreshToken: String= "",
    @SerializedName("refreshtoken")
    val refreshtoken: String= "",
    @SerializedName("registrationFrom")
    val registrationFrom: String= "",
    @SerializedName("remarks")
    val remarks: String= "",
    @SerializedName("retentionMerchantOrder")
    val retentionMerchantOrder: String= "",
    @SerializedName("retentionUserId")
    val retentionUserId: Int = 0,
    @SerializedName("returnCharge")
    val returnCharge: Double = 0.0,
    @SerializedName("sms")
    val sms: Boolean = false,
    @SerializedName("smsCharge")
    val smsCharge: Double = 0.0,
    @SerializedName("sourceType")
    val sourceType: String= "",
    @SerializedName("statusId")
    val statusId: List<Int> = listOf(),
    @SerializedName("subCategoryId")
    val subCategoryId: Int = 0,
    @SerializedName("teleSalesDate")
    val teleSalesDate: String= "",
    @SerializedName("thanaId")
    val thanaId: Int = 0,
    @SerializedName("thanaName")
    val thanaName: String = "",
    @SerializedName("token")
    val token: String= "",
    @SerializedName("userName")
    val userName: String= "",
    @SerializedName("vouchersViewModel")
    val vouchersViewModel: String= "",
    @SerializedName("webURL")
    val webURL: String = "",
    @SerializedName("weightRangeId")
    val weightRangeId: Int = 0
)