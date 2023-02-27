package com.ajkerdeal.app.ajkerdealadmin.api

import com.ajkerdeal.app.ajkerdealadmin.api.models.ErrorResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsResponse
import com.haroldadmin.cnradapter.NetworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.*

interface ApiInterfaceADM {

    companion object {
        operator fun invoke(retrofit: Retrofit): ApiInterfaceADM {
            return retrofit.create(ApiInterfaceADM::class.java)
        }
    }

    @POST("api/ComplainInsert/InsertDTComplain")
    suspend fun submitComplain(@Body requestBody: ComplainRequest): NetworkResponse<Int, ErrorResponse>

    @POST("api/Complain/UpdateComplainTypeWithOpenClosedForApp")
    suspend fun updateComplain(@Body requestBody: ComplainUpdateRequest): NetworkResponse<Int, ErrorResponse>

    @POST("api/Complain/GetAllComplainForAdminApp")
    suspend fun fetchComplainList(@Body requestBody: ComplainListRequest):NetworkResponse<List<ComplainData>, ErrorResponse>

    @POST("api/Complain/ComplainListNew")
    suspend fun fetchMerchantComplainList(@Body requestBody: MerchantComplainListRequest):NetworkResponse<List<MerchantComplainData>, ErrorResponse>

    @PUT("api/Complain/UpdateCommentsForAdminApp")
    suspend fun updateCommentsForAdminApp(@Body requestBody: ComplainCommentUpdateRequest):NetworkResponse<Int, ErrorResponse>

    @GET("api/Complain/GetAllCommentsForOnlyDTComplain/{bookingCode}")
    suspend fun getComplainHistory(@Path("bookingCode") bookingCode: Int): NetworkResponse<List<ComplainHistoryData>, ErrorResponse>

    @GET("api/Complain/GetAllCommentsDTComplainForApp/{bookingCode}/{isVisibleToMerchant}")
    suspend fun getAllCommentsDTComplainForApp(@Path("bookingCode") bookingCode: Int, @Path("isVisibleToMerchant") isVisibleToMerchant: Int): NetworkResponse<List<ComplainHistoryData>, ErrorResponse>

    @Multipart
    @POST("Image/ImageUploadForFile")
    suspend fun uploadProfilePhoto(
        @Part("ImageUrl") ImageUrl: RequestBody,
        @Part("FileName") FileName: RequestBody,
        @Part file: MultipartBody.Part? = null
    ) : NetworkResponse<Boolean, ErrorResponse>

    @POST("api/ComplainReports/AutoAssignedComplainCount")
    suspend fun autoAssignedComplainCount(@Body requestBody: AutoAssignComplainCountRequest): NetworkResponse<List<AutoAssignComplainCountResponse>, ErrorResponse>

    @POST("api/ComplainReports/AutoAssignedCountDetails")
    suspend fun autoAssignedCountDetails(@Body requestBody: AutoAssignedCountDetailsRequest): NetworkResponse<List<AutoAssignedCountDetailsResponse>, ErrorResponse>
}