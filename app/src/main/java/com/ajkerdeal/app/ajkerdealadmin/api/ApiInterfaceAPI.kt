package com.ajkerdeal.app.ajkerdealadmin.api

import com.ajkerdeal.app.ajkerdealadmin.api.models.ErrorResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.ResponseHeader
import com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection.CategoryModel
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterfaceAPI {

    companion object {
        operator fun invoke(retrofit: Retrofit): ApiInterfaceAPI {
            return retrofit.create(ApiInterfaceAPI::class.java)
        }
    }

    @GET("AppApi/Categories/v2/GetAllCategoriesSubCategoiesAndSubSubCategories/{categoryId}/{type}")
    suspend fun loadAllCategories(@Path("categoryId") categoryId:Int = 0, @Path("type") type:Int = 0): NetworkResponse<ResponseHeader<List<CategoryModel>>, ErrorResponse>

}