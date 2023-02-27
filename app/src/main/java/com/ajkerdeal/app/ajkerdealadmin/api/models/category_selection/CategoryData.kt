package com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection

data class CategoryData(
    var id: Int = 0,
    var displayNameBangla: String? = "",
    var displayNameEng: String? = "",
    var displayCategoryId: Int? = 0,
    var searchKey: String = "", // lower case
    var routingPath: String = ""
)
