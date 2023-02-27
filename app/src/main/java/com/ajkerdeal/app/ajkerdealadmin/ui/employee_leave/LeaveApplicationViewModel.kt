package com.ajkerdeal.app.ajkerdealadmin.ui.employee_leave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMTokenResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend.LeaveRecommendStatusUpdateReq
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend.LeaveRecommendStatusUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.leave_count.LeaveCountResponse
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LeaveApplicationViewModel(private val repository: AppRepository): ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    private val messageServerError = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    private val messageNetworkError = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    private val messageUnknownError = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    fun startEmployeeLeaveSession(requestBody: LeaveSessionRequest): LiveData<LeaveSessionResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<LeaveSessionResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.startEmployeeLeaveSession(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        viewState.value = ViewState.ShowMessage(messageServerError)
                    }
                    is NetworkResponse.NetworkError -> {
                        viewState.value = ViewState.ShowMessage(messageNetworkError)
                    }
                    is NetworkResponse.UnknownError -> {
                        viewState.value = ViewState.ShowMessage(messageUnknownError)
                        Timber.d(response.error)
                    }
                }
            }
        }

        return responseBody
    }

    fun fetchEmployeeLeaveSessionLists(requestBody: LeaveListRequest): LiveData<List<LeaveListsItem>> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<LeaveListsItem>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.fetchEmployeeLeaveSessionLists(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        viewState.value = ViewState.ShowMessage(messageServerError)
                    }
                    is NetworkResponse.NetworkError -> {
                        viewState.value = ViewState.ShowMessage(messageNetworkError)
                    }
                    is NetworkResponse.UnknownError -> {
                        viewState.value = ViewState.ShowMessage(messageUnknownError)
                        Timber.d(response.error)
                    }
                }
            }
        }

        return responseBody
    }

    fun updateLeaveStatus(requestBody: LeaveStatusUpdateRequest): LiveData<LeaveStatusUpdateResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<LeaveStatusUpdateResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateLeaveStatus(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        viewState.value = ViewState.ShowMessage(messageServerError)
                    }
                    is NetworkResponse.NetworkError -> {
                        viewState.value = ViewState.ShowMessage(messageNetworkError)
                    }
                    is NetworkResponse.UnknownError -> {
                        viewState.value = ViewState.ShowMessage(messageUnknownError)
                        Timber.d(response.error)
                    }
                }
            }
        }

        return responseBody
    }

    fun fetchEmployeeLeaveCount(userId: Int): LiveData<LeaveCountResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<LeaveCountResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getEmployeeLeaveCount(userId)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        val message = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                        viewState.value = ViewState.ShowMessage(message)
                    }
                    is NetworkResponse.NetworkError -> {
                        val message = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
                        viewState.value = ViewState.ShowMessage(message)
                    }
                    is NetworkResponse.UnknownError -> {
                        val message = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"
                        viewState.value = ViewState.ShowMessage(message)
                        Timber.d(response.error)
                    }
                }
            }
        }

        return responseBody
    }

    fun recommendStatusUpdate(requestBody: LeaveRecommendStatusUpdateReq): LiveData<LeaveRecommendStatusUpdateResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<LeaveRecommendStatusUpdateResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.recommendStatusUpdate(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        viewState.value = ViewState.ShowMessage(messageServerError)
                    }
                    is NetworkResponse.NetworkError -> {
                        viewState.value = ViewState.ShowMessage(messageNetworkError)
                    }
                    is NetworkResponse.UnknownError -> {
                        viewState.value = ViewState.ShowMessage(messageUnknownError)
                        Timber.d(response.error)
                    }
                }
            }
        }

        return responseBody
    }

    fun userfirebasetoken(userId: Int): LiveData<FCMTokenResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<FCMTokenResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.userfirebasetoken(userId)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        viewState.value = ViewState.ShowMessage(messageServerError)
                    }
                    is NetworkResponse.NetworkError -> {
                        viewState.value = ViewState.ShowMessage(messageNetworkError)
                    }
                    is NetworkResponse.UnknownError -> {
                        viewState.value = ViewState.ShowMessage(messageUnknownError)
                        Timber.d(response.error)
                    }
                }
            }
        }
        return responseBody
    }

}