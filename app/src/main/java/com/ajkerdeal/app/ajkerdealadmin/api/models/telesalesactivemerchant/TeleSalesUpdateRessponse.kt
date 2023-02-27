package com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant


import com.google.gson.annotations.SerializedName

data class TeleSalesUpdateRessponse(
    @SerializedName("accountName")
    val accountName: String = "",
    @SerializedName("accountNumber")
    val accountNumber: String = "",
    @SerializedName("acquisitionUserId")
    val acquisitionUserId: Int = 0,
    @SerializedName("address")
    val address: String = "",
    @SerializedName("advancePayment")
    val advancePayment: Double = 0.0,
    @SerializedName("alterMobile")
    val alterMobile: String = "",
    @SerializedName("bankName")
    val bankName: String = "",
    @SerializedName("bkashNumber")
    val bkashNumber: String = "",
    @SerializedName("blockReason")
    val blockReason: String = "",
    @SerializedName("branchName")
    val branchName: String = "",
    @SerializedName("categoryId")
    val categoryId: Int = 0,
    @SerializedName("collectionCharge")
    val collectionCharge: Double = 0.0,
    @SerializedName("companyName")
    val companyName: String = "",
    @SerializedName("courierUserId")
    val courierUserId: Int = 0,
    @SerializedName("credit")
    val credit: Double = 0.0,
    @SerializedName("customerSMSLimit")
    val customerSMSLimit: Int = 0,
    @SerializedName("customerVoiceSmsLimit")
    val customerVoiceSmsLimit: Int = 0,
    @SerializedName("deliveryRangeIdIOutside")
    val deliveryRangeIdIOutside: Int = 0,
    @SerializedName("deliveryRangeIdInside")
    val deliveryRangeIdInside: Int = 0,
    @SerializedName("districtId")
    val districtId: Int = 0,
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
    val isBreakAble: Boolean = false,
    @SerializedName("isCustomerEmail")
    val isCustomerEmail: Boolean = false,
    @SerializedName("isCustomerSms")
    val isCustomerSms: Boolean = false,
    @SerializedName("isDocument")
    val isDocument: Boolean = false,
    @SerializedName("isEmail")
    val isEmail: Boolean = false,
    @SerializedName("isHeavyWeight")
    val isHeavyWeight: Boolean = false,
    @SerializedName("isLoanActive")
    val isLoanActive: Boolean = false,
    @SerializedName("isOfferActive")
    val isOfferActive: Boolean = false,
    @SerializedName("isQuickOrderActive")
    val isQuickOrderActive: Boolean = false,
    @SerializedName("isSms")
    val isSms: Boolean = false,
    @SerializedName("isTeleSales")
    val isTeleSales: Boolean = false,
    @SerializedName("joinDate")
    val joinDate: String = "",
    @SerializedName("knowingSource")
    val knowingSource: Any = Any(),
    @SerializedName("loanCompany")
    val loanCompany: String = "",
    @SerializedName("mailCharge")
    val mailCharge: Double = 0.0,
    @SerializedName("maxCodCharge")
    val maxCodCharge: Double = 0.0,
    @SerializedName("merchantAssignActive")
    val merchantAssignActive: Boolean = false,
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
    @SerializedName("orderType")
    val orderType: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("preferredPaymentCycle")
    val preferredPaymentCycle: String = "",
    @SerializedName("preferredPaymentCycleDate")
    val preferredPaymentCycleDate: String = "",
    @SerializedName("priority")
    val priority: Any = Any(),
    @SerializedName("refereeEndTime")
    val refereeEndTime: Any = Any(),
    @SerializedName("refereeOrder")
    val refereeOrder: Int = 0,
    @SerializedName("refereeStartTime")
    val refereeStartTime: Any = Any(),
    @SerializedName("referrer")
    val referrer: String = "",
    @SerializedName("referrerEndTime")
    val referrerEndTime: String = "",
    @SerializedName("referrerIsActive")
    val referrerIsActive: Boolean = false,
    @SerializedName("referrerOrder")
    val referrerOrder: Int = 0,
    @SerializedName("referrerStartTime")
    val referrerStartTime: String = "",
    @SerializedName("refreshtoken")
    val refreshtoken: String = "",
    @SerializedName("registrationFrom")
    val registrationFrom: String = "",
    @SerializedName("remarks")
    val remarks: String = "",
    @SerializedName("retentionUserId")
    val retentionUserId: Int = 0,
    @SerializedName("returnCharge")
    val returnCharge: Double = 0.0,
    @SerializedName("routingNumber")
    val routingNumber: String = "",
    @SerializedName("smsCharge")
    val smsCharge: Double = 0.0,
    @SerializedName("sourceType")
    val sourceType: String = "",
    @SerializedName("subCategoryId")
    val subCategoryId: Int = 0,
    @SerializedName("teleSales")
    val teleSales: Int = 0,
    @SerializedName("teleSalesDate")
    val teleSalesDate: String = "",
    @SerializedName("thanaId")
    val thanaId: Int = 0,
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("webURL")
    val webURL: String = "",
    @SerializedName("weightRangeId")
    val weightRangeId: Int = 0
)