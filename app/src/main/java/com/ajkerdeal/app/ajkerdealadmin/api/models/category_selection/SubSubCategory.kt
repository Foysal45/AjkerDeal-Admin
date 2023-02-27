package com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection


import com.google.gson.annotations.SerializedName

data class SubSubCategory(
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("SubCategoryId")
    var subCategoryId: Int = 0,
    @SerializedName("SubSubCategoryId")
    var subSubCategoryId: Int = 0,
    @SerializedName("SubSubCategoryName")
    var subSubCategoryName: String? = "",
    @SerializedName("SubSubCategoryNameInEnglish")
    var subSubCategoryNameInEnglish: String? = "",
    @SerializedName("SubSubCategoryIcon")
    var subSubCategoryIcon: String? = "",
    @SerializedName("RoutingName")
    var routingName: String? = ""
)