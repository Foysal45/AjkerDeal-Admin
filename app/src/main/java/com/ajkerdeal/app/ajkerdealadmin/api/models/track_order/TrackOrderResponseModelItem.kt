package com.ajkerdeal.app.ajkerdealadmin.api.models.track_order


import com.google.gson.annotations.SerializedName

data class TrackOrderResponseModelItem(
    @SerializedName("comment")
    val comment: String = "",
    @SerializedName("courierId")
    val courierId: Int = 0,
    @SerializedName("courierOrderId")
    val courierOrderId: String = "",
    @SerializedName("hubName")
    val hubName: String = "",
    @SerializedName("isConfirmedBy")
    val isConfirmedBy: String = "",
    @SerializedName("namePostedBy")
    val namePostedBy: String = "",
    @SerializedName("orderDate")
    val orderDate: String = "",
    @SerializedName("podNumber")
    val podNumber: String = "",
    @SerializedName("postedOn")
    val postedOn: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("statusNameBng")
    val statusNameBng: String = "",
    @SerializedName("statusNameEng")
    val statusNameEng: String = "",
    @SerializedName("courierDeliveryManName")
    val courierDeliveryManName: String = "",
    @SerializedName("courierDeliveryManMobile")
    val courierDeliveryManMobile: String = "",
    @SerializedName("districtsViewModel")
    val districtsViewModel: DistrictsModel = DistrictsModel()

)