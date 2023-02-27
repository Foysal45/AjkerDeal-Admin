package com.ajkerdeal.app.ajkerdealadmin.repository

import com.ajkerdeal.app.ajkerdealadmin.api.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.AdUserReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.BreakRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.bulk_status.StatusUpdateData
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeSessionStartStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.LoginRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.MerchantComplainListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUsersInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session.LastDaySessionStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.key_word_survey.UserSearchKeyAddRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveSessionRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveStatusUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.UpdateLoanSurveyRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock.TokenGenerateUserLoginRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.MerchantUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.UpdateMerchantInformationRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.QuickOrderRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.TimeSlotRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.TimeSlotUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info.CalledInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.OrderWiseRetentionMerchantListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchantListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info.VisitedInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesActiveMerchantRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.SelectedCourierModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPymentRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.dashboard.DashBoardReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend.LeaveRecommendStatusUpdateReq
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetLoanSurveyRequestBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CODReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TelesalesRequestBody
import com.bd.deliverytiger.app.api.model.loan_survey.LoanSurveyRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Path

class AppRepository(
    private val apiInterfaceAPI: ApiInterfaceAPI,
    private val apiInterfaceANA: ApiInterfaceANA,
    private val apiInterfaceCore: ApiInterfaceCore,
    private val apiInterfaceADM: ApiInterfaceADM,
    private val apiInterfaceFCM: ApiInterfaceFCM
) {

    //#region Analytics
    suspend fun employeeLogin(requestBody: LoginRequest) = apiInterfaceANA.employeeLogin(requestBody)

    suspend fun employeeInformationUpdate(requestBody: ProfileUpdateRequest) = apiInterfaceANA.employeeInformationUpdate(requestBody)

    suspend fun getEmployeeStatus(userId: Int) = apiInterfaceANA.getEmployeeStatus(userId)

    suspend fun getEmployeeWorkReportData(date: String) = apiInterfaceANA.getEmployeeWorkReportData(date)

    suspend fun getEmployeeIsStatus(userId: Int) = apiInterfaceANA.getEmployeeIsStatus(userId)

    suspend fun getEmployeeWorkingTime(userId: Int) = apiInterfaceANA.getEmployeeWorkingTime(userId)

    suspend fun startEmployeeBreakSession(requestBody: BreakRequest) = apiInterfaceANA.startEmployeeBreakSession(requestBody)

    suspend fun startStopEmployeeSession(requestBody: EmployeeSessionStartStopRequest) = apiInterfaceANA.startStopEmployeeSession(requestBody)

    suspend fun startEmployeeLeaveSession(requestBody: LeaveSessionRequest) = apiInterfaceANA.startEmployeeLeaveSession(requestBody)

    suspend fun fetchEmployeeLeaveSessionLists(requestBody: LeaveListRequest) = apiInterfaceANA.fetchEmployeeLeaveSessionLists(requestBody)

    suspend fun updateLeaveStatus(requestBody: LeaveStatusUpdateRequest) = apiInterfaceANA.updateLeaveStatus(requestBody)

    suspend fun lastSessionEmergencyEnd(requestBody: LastDaySessionStopRequest) = apiInterfaceANA.lastSessionEmergencyEnd(requestBody)

    suspend fun getEmployeeLeaveCount(userId: Int) = apiInterfaceANA.getEmployeeLeaveCount(userId)

    suspend fun getEmployeeLeaveReport() = apiInterfaceANA.getEmployeeLeaveReport()

    suspend fun addUserSearchKey(requestBody: UserSearchKeyAddRequest) = apiInterfaceANA.addUserSearchKey(requestBody)

    suspend fun recommendStatusUpdate(reqBody: LeaveRecommendStatusUpdateReq) = apiInterfaceANA.recommendStatusUpdate(reqBody)
    //#endregion

    //#region API
    suspend fun loadAllCategories(categoryId: Int, type: Int) = apiInterfaceAPI.loadAllCategories(categoryId, type)
    //#endregion

    //#region ADCORE
    suspend fun getRetentionMerchantList(requestBody: RetentionMerchantListRequest) = apiInterfaceCore.getRetentionMerchantList(requestBody)

    suspend fun getOrderWiseRetentionMerchantList(requestBody: OrderWiseRetentionMerchantListRequest) = apiInterfaceCore.getOrderWiseRetentionMerchantList(requestBody)

    suspend fun updateVisitedMerchant(requestBody: VisitedInfoRequest) = apiInterfaceCore.updateVisitedMerchant(requestBody)

    suspend fun updateCalledMerchant(requestBody: CalledInfoRequest) = apiInterfaceCore.updateCalledMerchant(requestBody)

    suspend fun fetchAllHub() = apiInterfaceCore.fetchAllHub()

    suspend fun updateBulkStatus(requestBody: List<StatusUpdateData>) = apiInterfaceCore.updateBulkStatus(requestBody)


    //Quick Order
    suspend fun getCollectionTimeSlot(requestBody: TimeSlotRequest) = apiInterfaceCore.getCollectionTimeSlot(requestBody)

    suspend fun quickOrderRequest(requestBody: QuickOrderRequest) = apiInterfaceCore.quickOrderRequest(requestBody)

    suspend fun getPickupLocations(courierUserId: Int) = apiInterfaceCore.getPickupLocations(courierUserId)

    suspend fun deleteOrderRequest(orderRequestId: Int) = apiInterfaceCore.deleteOrderRequest(orderRequestId)

    suspend fun updateMultipleTimeSlot(requestBody: List<TimeSlotUpdateRequest>) = apiInterfaceCore.updateMultipleTimeSlot(requestBody)

    suspend fun fetchCourierList() = apiInterfaceCore.fetchCourierList()

    suspend fun submitCourierList(requestBody: List<SelectedCourierModel>) = apiInterfaceCore.submitCourierList(requestBody)

    suspend fun UpdateCourierWithLoanSurvey(requestBody: List<SelectedCourierModel>, loanSurveyId: Int, auth:String) = apiInterfaceCore.UpdateCourierWithLoanSurvey(requestBody, loanSurveyId, auth)

    suspend fun imageUploadForFile(
        fileName: RequestBody,
        imagePath: RequestBody,
        file: MultipartBody.Part?
    ) = apiInterfaceADM.uploadProfilePhoto(fileName, imagePath, file)

    //#endregion


    //#region ADM (Ajkerdeal Admin)
    suspend fun uploadProfileImage(imageUrl: RequestBody, fileName: RequestBody, file: MultipartBody.Part? = null) = apiInterfaceADM.uploadProfilePhoto(imageUrl, fileName, file)

    suspend fun submitComplain(requestBody: ComplainRequest) = apiInterfaceADM.submitComplain(requestBody)

    suspend fun updateComplain(requestBody: ComplainUpdateRequest) = apiInterfaceADM.updateComplain(requestBody)

    suspend fun updateCommentsForAdminApp(requestBody: ComplainCommentUpdateRequest) = apiInterfaceADM.updateCommentsForAdminApp(requestBody)

    suspend fun fetchComplainList(requestBody: ComplainListRequest) = apiInterfaceADM.fetchComplainList(requestBody)

    suspend fun fetchMerchantComplainList(requestBody: MerchantComplainListRequest) = apiInterfaceADM.fetchMerchantComplainList(requestBody)

    suspend fun getComplainHistory(bookingCode: Int) = apiInterfaceADM.getComplainHistory(bookingCode)

    suspend fun getAllCommentsDTComplainForApp(bookingCode: Int, isVisibleToMerchant: Int) = apiInterfaceADM.getAllCommentsDTComplainForApp(bookingCode, isVisibleToMerchant)
    //#endregion

    //#region Retention Report
    suspend fun getRetentionMerchantFollowUpDetails(requestBody: RetentionReportReqBody) = apiInterfaceCore.getRetentionMerchantFollowUpDetails(requestBody)

    suspend fun getRetentionMerchantFollowUpReportDetails(requestBody: RetentionReportDetailsReqBody) = apiInterfaceCore.getRetentionMerchantFollowUpReportDetails(requestBody)
    //#endregion

    //#region Track Order
    suspend fun trackOrder(orderId: String?) = apiInterfaceCore.trackOrder(orderId)
    //#endRegion

    suspend fun sendPushNotifications(authToken: String, @Body requestBody: FCMRequest) = apiInterfaceFCM.sendPushNotifications(authToken, requestBody)

    //suspend fun getTelesalesActiveMerchantList(status: Int, date: String) = apiInterfaceCore.telesalesActiveMerchantList(status, date)

    //suspend fun getTelesalesActiveMerchantList(requestBody: TeleSalesActiveMerchantRequest) = apiInterfaceCore.telesalesActiveMerchantList(requestBody)

    //Instant Payment Information
    suspend fun getInstantPaymentInformation(requestBody: InstantPymentRequest) = apiInterfaceCore.instantPaymentInformation(requestBody)

    suspend fun getSrWiseCourierUsersInfo(requestBody: MerchantUpdateRequest) = apiInterfaceCore.getSrWiseCourierUsersInfo(requestBody)

    suspend fun loadAllDistricts() = apiInterfaceCore.loadAllDistricts()

    suspend fun loadAllDistrictsById(id: Int) = apiInterfaceCore.loadAllDistrictsById(id)

    suspend fun getPickupLocation(userID: Int) = apiInterfaceCore.getPickupLocation(userID)

    suspend fun getAdUsers()  = apiInterfaceCore.getAdUsers()

    suspend fun getAdUsersByFilter(requestBody: AdUserReqBody)  = apiInterfaceCore.getAdUsersByFilter(requestBody)

    suspend fun getRiders()  = apiInterfaceCore.getRiders()

    suspend fun updatePickupLocations(id: Int, requestBody: MerchantPickupLocation) = apiInterfaceCore.updatePickupLocations(id, requestBody)

    suspend fun updateMerchantInformation(id: Int, requestBody: UpdateMerchantInformationRequest) = apiInterfaceCore.updateMerchantInformation(id, requestBody)

    suspend fun addPickupLocations(requestBody: MerchantPickupLocation) = apiInterfaceCore.addPickupLocations(requestBody)

    suspend fun  deletePickupLocations(id: Int) = apiInterfaceCore.deletePickupLocations(id)

    suspend fun updateTelesalesStatus(requestBody: TeleSalesUpdateRequest, userId: Int) = apiInterfaceCore.updateTelesalesStatus(requestBody, userId)

    suspend fun autoAssignedComplainCount(requestBody: AutoAssignComplainCountRequest) = apiInterfaceADM.autoAssignedComplainCount(requestBody)

    suspend fun autoAssignedComplainCountDetails(requestBody: AutoAssignedCountDetailsRequest) = apiInterfaceADM.autoAssignedCountDetails(requestBody)

    suspend fun getOrderCount(reqBody: OrderCountReqBody) = apiInterfaceCore.getOrderCount(reqBody)

    suspend fun getDataDuration() = apiInterfaceCore.getDataDuration()

    suspend fun getAllOrder(body: CODReqBody) = apiInterfaceCore.getAllOrder(body)

    suspend fun getLoanSurveyData(courierUserId: Int) = apiInterfaceCore.getLoanSurveyData(courierUserId)

    suspend fun updateLoanSurvey(loanSurveyId: Int, requestBody: UpdateLoanSurveyRequest, auth:String) = apiInterfaceCore.updateLoanSurvey(loanSurveyId, requestBody, auth)

    suspend fun tokenGenerateUserLogin(requestBody: TokenGenerateUserLoginRequest) = apiInterfaceCore.tokenGenerateUserLogin( requestBody)

    suspend fun getDashboardStatusGroup(requestBody: DashBoardReqBody) = apiInterfaceCore.getDashboardStatusGroup(requestBody)

    suspend fun GetLoanSurveyI(requestBody: GetLoanSurveyRequestBody) = apiInterfaceCore.GetLoanSurveyI(requestBody)

    suspend fun GetCourierUserInfo(req: Int) = apiInterfaceCore.GetCourierUserInfo(req)

    suspend fun MonthWiseTotalCollectionAmount(req: Int) = apiInterfaceCore.MonthWiseTotalCollectionAmount(req)

    suspend fun getTelesalesActiveMerchantList(req: TelesalesRequestBody) = apiInterfaceCore.getTelesalesActiveMerchantList(req)

    suspend fun telesalesDetails(teleSalesStatus: Int, date: String) = apiInterfaceCore.telesalesDetails(teleSalesStatus, date)

    suspend fun submitLoanSurvey(requestBody: UpdateLoanSurveyRequest) = apiInterfaceCore.submitLoanSurvey(requestBody)

    suspend fun userfirebasetoken(userId: Int) = apiInterfaceANA.userfirebasetoken(userId)

    suspend fun getCourierUsersInfo(requestBody: CourierUsersInfoRequest) = apiInterfaceCore.getCourierUsersInfo(requestBody)

}