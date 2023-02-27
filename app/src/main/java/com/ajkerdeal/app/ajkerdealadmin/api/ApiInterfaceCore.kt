package com.ajkerdeal.app.ajkerdealadmin.api

import com.ajkerdeal.app.ajkerdealadmin.api.models.ErrorResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.GenericResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.AdUserReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.bulk_status.StatusUpdateData
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUsersInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUsersInfoResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.hub.HubInfo
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.LoanSurveyResponseModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.UpdateLoanSurveyRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock.TokenGenerateUserLoginRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock.TokenGenerateUserLoginResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.MerchantInfoUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.MerchantUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.SrWiseCurrierUserInfoModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.UpdateMerchantInformationRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.DistrictModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.pickup_location.PickupLocation
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info.CalledInfoData
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info.CalledInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info.VisitedInfoData
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info.VisitedInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.riders.RiderModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.track_order.TrackOrderResponseModelItem
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPaymentModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPymentRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.CourierModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.SelectedCourierModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.dashboard.DashBoardReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.dashboard.DashboardData
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetCourrierUsersInfoResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetLoanSurveyRequestBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetloanSurveyResponseBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.MonthlyTransactionLoanSurveyResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CODReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CODResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.*
import com.bd.deliverytiger.app.api.model.config.BannerResponse
import com.bd.deliverytiger.app.api.model.loan_survey.LoanSurveyRequestBody
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.Retrofit
import retrofit2.http.*

interface ApiInterfaceCore {

    companion object {
        operator fun invoke(retrofit: Retrofit): ApiInterfaceCore {
            return retrofit.create(ApiInterfaceCore::class.java)
        }
    }

    //BaseUrl https://adcore.ajkerdeal.com

    @POST("api/Retention/GetRetentionMerchantListV1")
    suspend fun getRetentionMerchantList(@Body requestBody: RetentionMerchantListRequest): NetworkResponse<GenericResponse<List<RetentionMerchentListModel>>, ErrorResponse>

    @POST("/api/Retention/GetOrderedRetentionMerchantList")
    suspend fun getOrderWiseRetentionMerchantList(@Body requestBody: OrderWiseRetentionMerchantListRequest): NetworkResponse<GenericResponse<List<RetentionMerchentListModel>>, ErrorResponse>

    @POST("api/Retention/AddVisitedMerchant")
    suspend fun updateVisitedMerchant(@Body requestBody: VisitedInfoRequest): NetworkResponse<GenericResponse<VisitedInfoData>, ErrorResponse>

    @POST("api/Retention/AddCalledMerchant")
    suspend fun updateCalledMerchant(@Body requestBody: CalledInfoRequest): NetworkResponse<GenericResponse<CalledInfoData>, ErrorResponse>

    @GET("api/Fetch/GetAllHubs")
    suspend fun fetchAllHub(): NetworkResponse<GenericResponse<List<HubInfo>>, ErrorResponse>

    @PUT("api/Update/UpdateBulkStatus")
    suspend fun updateBulkStatus(@Body requestBody: List<StatusUpdateData>): NetworkResponse<GenericResponse<Int>, ErrorResponse>

    //Track Order from Chat
    @GET("api/Fetch/OrderUpdateHistory/{CourierOrderId}")
    suspend fun trackOrder(@Path("CourierOrderId") courierOrderId: String?): NetworkResponse<GenericResponse<ArrayList<TrackOrderResponseModelItem>>, ErrorResponse>

    //Quick Order Request
    @POST("api/Fetch/GetCollectionTimeSlotByTime")
    suspend fun getCollectionTimeSlot(@Body requestBody: TimeSlotRequest): NetworkResponse<GenericResponse<List<QuickOrderTimeSlotData>>, ErrorResponse>

    @POST("api/Entry/AddOrderRequest")
    suspend fun quickOrderRequest(@Body requestBody: QuickOrderRequest): NetworkResponse<GenericResponse<QuickOrderRequestResponse>, ErrorResponse>

    @GET("api/Fetch/GetPickupLocations/{courierUserId}")
    suspend fun getPickupLocations(@Path("courierUserId") courierUserId: Int): NetworkResponse<GenericResponse<List<PickupLocation>>, ErrorResponse>

    @DELETE("api/QuickOrder/DeleteOrderRequest/{orderRequestId}")
    suspend fun deleteOrderRequest(@Path("orderRequestId") orderRequestId: Int): NetworkResponse<GenericResponse<Int>, ErrorResponse>

    @PUT("api/QuickOrder/UpdateMultipleTimeSlot")
    suspend fun updateMultipleTimeSlot(@Body requestBody: List<TimeSlotUpdateRequest>): NetworkResponse<GenericResponse<Int>, ErrorResponse>

    //Telesales
    /*   @POST("/api/Report/TelesalesDetails/{teleSalesStatus}/{date}")
       suspend fun telesalesActiveMerchantList(@Path("teleSalesStatus") teleSalesStatus: Int, @Path("date") date: String): NetworkResponse<GenericResponse<List<TelesalesMerchantInformation>>, ErrorResponse>
       */
    @POST("/api/Report/StatusWiseTelesalesDetails")
    suspend fun telesalesActiveMerchantList(@Body requestBody: TeleSalesActiveMerchantRequest): NetworkResponse<GenericResponse<List<TeleSalesActiveMerchantResponse>>, ErrorResponse>

    //Instant Payment
    @POST("/api/Report/GetPreferredPaymentCycle")
    suspend fun instantPaymentInformation(@Body requestBody: InstantPymentRequest): NetworkResponse<GenericResponse<List<InstantPaymentModel>>, ErrorResponse>

    //Merchant Info
    @POST("/api/Retention/GetSrWiseCourierUsersInfo")
    suspend fun getSrWiseCourierUsersInfo(@Body requestBody: MerchantUpdateRequest): NetworkResponse<GenericResponse<SrWiseCurrierUserInfoModel>, ErrorResponse>

    // District and thana load
    @GET("api/Fetch/LoadAllDistricts")
    suspend fun loadAllDistricts(): NetworkResponse<GenericResponse<List<DistrictModel>>, ErrorResponse>

    @GET("api/Fetch/LoadAllDistrictsById/{id}")
    suspend fun loadAllDistrictsById(@Path("id") id: Int): NetworkResponse<GenericResponse<List<DistrictModel>>, ErrorResponse>


    @GET("api/Fetch/GetPickupLocations/{userID}")
    suspend fun getPickupLocation(@Path("userID") userId: Int): NetworkResponse<GenericResponse<List<MerchantPickupLocation>>, ErrorResponse>

    @GET("api/Fetch/GetAdUsers")
    suspend fun getAdUsers(): NetworkResponse<GenericResponse<List<ADUsersModel>>, ErrorResponse>

    @POST("api/Fetch/GetAdUsersByFilter")
    suspend fun getAdUsersByFilter(@Body requestBody: AdUserReqBody): NetworkResponse<GenericResponse<List<ADUsersModel>>, ErrorResponse>

    @GET("api/Fetch/GetAllDeliveryMan")
    suspend fun getRiders(): NetworkResponse<GenericResponse<List<RiderModel>>, ErrorResponse>

    @POST("api/Entry/AddPickupLocations")
    suspend fun addPickupLocations(@Body requestBody: MerchantPickupLocation): NetworkResponse<GenericResponse<MerchantPickupLocation>, ErrorResponse>

    @PUT("/api/Update/UpdateMerchantInformation/{id}")
    suspend fun updateMerchantInformation(@Path("id") id: Int, @Body requestBody: UpdateMerchantInformationRequest): NetworkResponse<GenericResponse<MerchantInfoUpdateResponse>, ErrorResponse>

    @PUT("api/Update/UpdatePickupLocations/{id}")
    suspend fun updatePickupLocations(@Path("id") id: Int, @Body requestBody: MerchantPickupLocation): NetworkResponse<GenericResponse<MerchantPickupLocation>, ErrorResponse>

    @DELETE("api/Delete/DeletePickupLocations/{id}")
    suspend fun deletePickupLocations(@Path("id") id: Int): NetworkResponse<GenericResponse<Int>, ErrorResponse>


    //Retention Report
    @POST("api/Retention/NewRetentionMerchantFollowUpReport")
    suspend fun getRetentionMerchantFollowUpDetails(@Body requestBody: RetentionReportReqBody): NetworkResponse<GenericResponse<List<RetentionReportModel>>, ErrorResponse>

    @POST("api/Retention/NewRetentionMerchantFollowUpReportDetails")
    suspend fun getRetentionMerchantFollowUpReportDetails(@Body requestBody: RetentionReportDetailsReqBody): NetworkResponse<GenericResponse<List<RetentionReportDetailsModel>>, ErrorResponse>

    @PUT("api/Update/UpdateTelesalesStatus/{userId}")
    suspend fun updateTelesalesStatus(@Body requestBody: TeleSalesUpdateRequest, @Path("userId") userId: Int): NetworkResponse<GenericResponse<TeleSalesUpdateRessponse>, ErrorResponse>

    @GET("api/Dashboard/GetCouriers")
    suspend fun fetchCourierList(): NetworkResponse<GenericResponse<List<CourierModel>>, ErrorResponse>

    @POST("api/Entry/AddCouriersWithLoanSurvey")
    suspend fun submitCourierList(@Body requestBody: List<SelectedCourierModel>): NetworkResponse<List<SelectedCourierModel>, ErrorResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("api/Update/UpdateCourierWithLoanSurvey/{loanSurveyId}")
    suspend fun UpdateCourierWithLoanSurvey(
        @Body requestBody: List<SelectedCourierModel>,
        @Path("loanSurveyId") loanSurveyId: Int,
        @Header("Authorization") auth: String
    ): NetworkResponse<Int, ErrorResponse>

    @GET("api/Loan/GetLoanSurveyByCourierUser/{courierUserId}")
    suspend fun getLoanSurveyData(@Path("courierUserId") courierUserId: Int): NetworkResponse<GenericResponse<List<LoanSurveyResponseModel>>, ErrorResponse>

    //Loan Survey Update
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("/api/Update/UpdateLoanSurvey/{loanSurveyId}")
    suspend fun updateLoanSurvey(
        @Path("loanSurveyId") loanSurveyId: Int,
        @Body requestBody: UpdateLoanSurveyRequest,
        @Header("Authorization") auth: String
    ): NetworkResponse<GenericResponse<Int>, ErrorResponse>

    @POST("api/Account/UserLogin")
    suspend fun tokenGenerateUserLogin(@Body request: TokenGenerateUserLoginRequest): NetworkResponse<GenericResponse<TokenGenerateUserLoginResponse>, ErrorResponse>

    //merchant follow up
    @GET("api/Dashboard/GetAllBanners")
    suspend fun getDataDuration(): NetworkResponse<GenericResponse<BannerResponse>, ErrorResponse>

    @POST("api/Dashboard/GetOrderCountByStatusGroupV3")
    suspend fun getOrderCount(@Body requestBody: OrderCountReqBody): NetworkResponse<GenericResponse<List<OrderCountResponse>>, ErrorResponse>

    @POST("api/Fetch/GetAllOrders")
    suspend fun getAllOrder(@Body body: CODReqBody): NetworkResponse<GenericResponse<CODResponse>, ErrorResponse>

    @POST("api/Dashboard/GetOrderCountByStatusGroupV3")
    suspend fun getDashboardStatusGroup(@Body requestBody: DashBoardReqBody): NetworkResponse<GenericResponse<List<DashboardData>>, ErrorResponse>

    @POST("api/Report/GetLoanSurvey")
    suspend fun GetLoanSurveyI(@Body requestBody: GetLoanSurveyRequestBody): NetworkResponse<GenericResponse<List<GetloanSurveyResponseBody>>, ErrorResponse>

    @GET("api/Fetch/GetCourierUserInfo/{courierUserId}")
    suspend fun GetCourierUserInfo(@Path("courierUserId") courierUserId: Int): NetworkResponse<GenericResponse<GetCourrierUsersInfoResponse>, ErrorResponse>

    @GET("api/Report/MonthWiseTotalCollectionAmount/{CourierUserId}")
    suspend fun MonthWiseTotalCollectionAmount(@Path("CourierUserId") CourierUserId: Int): NetworkResponse<GenericResponse<List<MonthlyTransactionLoanSurveyResponse>>, ErrorResponse>

    @POST("api/Report/GetTelesalesActiveMerchantList")
    suspend fun getTelesalesActiveMerchantList(@Body requestBody: TelesalesRequestBody): NetworkResponse<GenericResponse<List<TeleSalesActiveMerchantResponse>>, ErrorResponse>

    @POST("/api/Report/TelesalesDetails/{teleSalesStatus}/{date}")
    suspend fun telesalesDetails(@Path("teleSalesStatus") teleSalesStatus: Int, @Path("date") date: String): NetworkResponse<GenericResponse<List<TeleSalesActiveMerchantDetailsResponse>>, ErrorResponse>

    @POST("api/Loan/AddLoanSurvey")
    suspend fun submitLoanSurvey(@Body requestBody: UpdateLoanSurveyRequest): NetworkResponse<GenericResponse<LoanSurveyResponseModel>, ErrorResponse>

    @POST("api/fetch/GetCourierUsersInfo")
    suspend fun getCourierUsersInfo(@Body requestBody: CourierUsersInfoRequest): NetworkResponse<GenericResponse<CourierUsersInfoResponse>, ErrorResponse>

}