package com.ajkerdeal.app.ajkerdealadmin.api


import com.ajkerdeal.app.ajkerdealadmin.api.models.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.BreakRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.BreakResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.EmployeeIsBreakResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeSessionResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeSessionStartStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeStatusResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeWorkTimeResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session.LastDaySessionStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session.LastDaySessionStopResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMTokenResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.key_word_survey.UserSearchKeyAddRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.key_word_survey.UserSearchKeyAddResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend.LeaveRecommendStatusUpdateReq
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend.LeaveRecommendStatusUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.LeaveReportResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.leave_count.LeaveCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.LoginRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.LoginResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.report.WorkReportData
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterfaceANA {

    companion object {
        operator fun invoke(retrofit: Retrofit): ApiInterfaceANA {
            return retrofit.create(ApiInterfaceANA::class.java)
        }
    }

    @POST("api/employeelogin/")
    suspend fun employeeLogin(@Body requestBody: LoginRequest): NetworkResponse<LoginResponse, ErrorResponse>

    @GET("api/employeeworkingreport/{date}")//Date format 20210428/2021-04-28
    suspend fun getEmployeeWorkReportData(@Path("date") date:String= "1900-01-01"): NetworkResponse<List<WorkReportData>,ErrorResponse>

    @GET("api/employeestatus/{Userid}")
    suspend fun getEmployeeStatus(@Path("Userid") Userid:Int= 0): NetworkResponse<EmployeeStatusResponse,ErrorResponse>

    @GET("api/employeeisbreak/{Userid}/")
    suspend fun getEmployeeIsStatus(@Path("Userid") Userid:Int= 0): NetworkResponse<EmployeeIsBreakResponse,ErrorResponse>

    @GET("api/employeeworkingtime/{Userid}/")
    suspend fun getEmployeeWorkingTime(@Path("Userid") Userid:Int= 0): NetworkResponse<EmployeeWorkTimeResponse,ErrorResponse>

    @POST("api/employeesession/")
    suspend fun startStopEmployeeSession(@Body startStopRequestBody: EmployeeSessionStartStopRequest): NetworkResponse<EmployeeSessionResponse, ErrorResponse>

     @POST("api/employeebreaksession/")
    suspend fun startEmployeeBreakSession(@Body startStopRequestBody: BreakRequest): NetworkResponse<BreakResponse, ErrorResponse>

    //Leave
    @POST("api/employeeleavesession/")
    suspend fun startEmployeeLeaveSession(@Body leaveStartRequestBody: LeaveSessionRequest): NetworkResponse<LeaveSessionResponse, ErrorResponse>

    @POST("api/employeeleavesessionlist/")
    suspend fun fetchEmployeeLeaveSessionLists(@Body leaveStartRequestBody: LeaveListRequest): NetworkResponse<List<LeaveListsItem>, ErrorResponse>

    @POST("/api/leavesessionstatusupdate/")
    suspend fun updateLeaveStatus(@Body leaveStartRequestBody: LeaveStatusUpdateRequest): NetworkResponse<LeaveStatusUpdateResponse, ErrorResponse>

    @POST("/api/emergencyend/")
    suspend fun lastSessionEmergencyEnd(@Body leaveStartRequestBody: LastDaySessionStopRequest): NetworkResponse<LastDaySessionStopResponse, ErrorResponse>

    @POST("api/employeeinformationupdate/")
    suspend fun employeeInformationUpdate(@Body requestBody: ProfileUpdateRequest): NetworkResponse<ProfileUpdateResponse, ErrorResponse>

    @GET("api/employeeleavecount/{UserId}/")
    suspend fun getEmployeeLeaveCount(@Path("UserId") UserId:Int= 0): NetworkResponse<LeaveCountResponse, ErrorResponse>

    @GET("/api/employeeleavereport/")
    suspend fun getEmployeeLeaveReport(): NetworkResponse<LeaveReportResponse, ErrorResponse>

    @POST("api/UserSearchkey/")
    suspend fun addUserSearchKey(@Body requestBody: UserSearchKeyAddRequest): NetworkResponse<UserSearchKeyAddResponse, ErrorResponse>

    @POST("api/leavesessionrecommendstatusupdate/")
    suspend fun recommendStatusUpdate(@Body requestBody: LeaveRecommendStatusUpdateReq): NetworkResponse<LeaveRecommendStatusUpdateResponse, ErrorResponse>

    @GET("api/userfirebasetoken/{UserId}/")
    suspend fun userfirebasetoken(@Path("UserId") UserId: Int): NetworkResponse<FCMTokenResponse, ErrorResponse>

}