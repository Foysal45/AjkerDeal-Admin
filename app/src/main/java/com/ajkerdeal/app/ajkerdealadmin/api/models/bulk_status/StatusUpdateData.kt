package com.ajkerdeal.app.ajkerdealadmin.api.models.bulk_status


import com.google.gson.annotations.SerializedName

data class StatusUpdateData(
    @SerializedName("updatedBy")
    var updatedBy: Int = 0,
    @SerializedName("courierOrdersId")
    var courierOrdersId: String = "",
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("isConfirmedBy")
    var isConfirmedBy: String = "",
    @SerializedName("hubName")
    var hubName: String = "",
    @SerializedName("comment")
    var comment: String = ""
)