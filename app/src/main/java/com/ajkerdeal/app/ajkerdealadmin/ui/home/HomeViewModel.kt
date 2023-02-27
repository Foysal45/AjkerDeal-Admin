package com.ajkerdeal.app.ajkerdealadmin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.BreakRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.break_model.BreakResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeSessionResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeSessionStartStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeStatusResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.EmployeeWorkTimeResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session.LastDaySessionStopRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.employee_session.last_day_session.LastDaySessionStopResponse
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeViewModel(private val repository: AppRepository): ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    private val messageServerError = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    private val messageNetworkError = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    private val messageUnknownError = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    fun getEmployeeStatus(userId: Int): LiveData<EmployeeStatusResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<EmployeeStatusResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getEmployeeStatus(userId)
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

    fun getEmployeeIsBreakStatus(userId: Int): LiveData<Boolean> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getEmployeeIsStatus(userId)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body.status
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

    fun lastSessionEmergencyEnd(requestBody: LastDaySessionStopRequest): LiveData<LastDaySessionStopResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<LastDaySessionStopResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.lastSessionEmergencyEnd(requestBody)
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

    fun getEmployeeWorkingTime(userId: Int): LiveData<EmployeeWorkTimeResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<EmployeeWorkTimeResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getEmployeeWorkingTime(userId)
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

    fun startStopEmployeeSession(startStopRequestBody: EmployeeSessionStartStopRequest): LiveData<EmployeeSessionResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<EmployeeSessionResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.startStopEmployeeSession(startStopRequestBody)
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

    fun startEmployeeBreakSession(startStopBreakRequestBody: BreakRequest): LiveData<BreakResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<BreakResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.startEmployeeBreakSession(startStopBreakRequestBody)
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