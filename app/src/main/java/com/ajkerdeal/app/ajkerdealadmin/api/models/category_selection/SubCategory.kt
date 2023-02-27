package com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection


import com.google.gson.annotations.SerializedName

data class SubCategory(
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("SubCategoryId")
    var subCategoryId: Int = 0,
    @SerializedName("SubCategoryName")
    var subCategoryName: String? = "",
    @SerializedName("SubCategoryNameInEnglish")
    var subCategoryNameInEnglish: String? = "",
    @SerializedName("SubCategoryIcon")
    var subCategoryIcon: String? = "",
    @SerializedName("RoutingName")
    var routingName: String? = "",
    @SerializedName("SubCatOrderBy")
    var subCatOrderBy: Int = 0,
    @SerializedName("SubSubCategory")
    var subSubCategory: List<SubSubCategory> = listOf()
)