package com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info


import com.google.gson.annotations.SerializedName

data class CourierUsersInfoRequest(
    @SerializedName("count")
    var count: Int = 0,
    @SerializedName("index")
    var index: Int = 0,
    @SerializedName("quickOrderGenerateBy")
    var quickOrderGenerateBy: Int = 0,
    @SerializedName("quickOrderGenerateForHub")
    var quickOrderGenerateForHub: String? = "",
    @SerializedName("quickOrderLimit")
    var quickOrderLimit: Int = 0,
    @SerializedName("retentionUserId")
    var retentionUserId: Int = 0,
    @SerializedName("search")
    var search: String? = ""
)