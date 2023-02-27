package com.ajkerdeal.app.ajkerdealadmin.api.models.key_word_survey


import com.google.gson.annotations.SerializedName

data class UserSearchKeyAddRequest(
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("SubCategoryId")
    var subCategoryId: Int = 0,
    @SerializedName("SubSubCategoryId")
    var subSubCategoryId: Int = 0,
    @SerializedName("CategoryEng")
    var categoryEng: String? = "",
    @SerializedName("SubCategoryEng")
    var subCategoryEng: String? = "",
    @SerializedName("SubSubCategoryEng")
    var subSubCategoryEng: String? = "",
    @SerializedName("RoutingName")
    var routingName: String? = "",
    @SerializedName("EngKeyword")
    var engKeyword: String? = "",
    @SerializedName("BngKeyword")
    var bngKeyword: String? = ""
)