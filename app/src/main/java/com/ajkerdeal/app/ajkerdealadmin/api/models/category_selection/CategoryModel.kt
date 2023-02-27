package com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection


import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("CategoryName")
    var categoryName: String? = "",
    @SerializedName("CategoryNameInEnglish")
    var categoryNameInEnglish: String? = "",
    @SerializedName("RoutingName")
    var routingName: String? = "",
    @SerializedName("BannerImage")
    var bannerImage: String? = "",
    @SerializedName("BannerActionUrl")
    var bannerActionUrl: String? = "",
    @SerializedName("IsSubCategoryAvailable")
    var isSubCategoryAvailable: Int = 0,
    @SerializedName("CategoryIcon")
    var categoryIcon: String? = "",
    @SerializedName("SubCategory")
    var subCategory: List<SubCategory> = listOf()
)