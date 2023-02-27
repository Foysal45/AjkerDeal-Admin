package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainData
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ComplainViewModel(private val repository: AppRepository): ViewModel() {
     var fromDate = ""
     var toDate = ""
    val viewState = MutableLiveData<ViewState>(ViewState.NONE)

    fun submitComplain(requestBody: ComplainRequest): LiveData<Int> {

        val responseData: MutableLiveData<Int> = MutableLiveData()

        viewState.value = ViewState.ProgressState(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.submitComplain(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
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
        return responseData
    }

    fun updateDtComplain(requestBody: ComplainUpdateRequest): LiveData<Int> {

        viewState.value = ViewState.ProgressState(true)
        val responseData: MutableLiveData<Int> = MutableLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateComplain(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
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
        return responseData
    }

    fun fetchComplainList(requestBody: ComplainListRequest): LiveData<List<ComplainData>> {

        val responseData: MutableLiveData<List<ComplainData>> = MutableLiveData()
        viewState.value = ViewState.ProgressState(true)

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.fetchComplainList(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        val message = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                        viewState.value = ViewState.ShowMessage(message)
                        responseData.value = listOf()
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
        return responseData
    }


    fun assignedComplainCount(requestBody: AutoAssignComplainCountRequest): LiveData<List<AutoAssignComplainCountResponse>> {

        val responseData: MutableLiveData<List<AutoAssignComplainCountResponse>> = MutableLiveData()
        viewState.value = ViewState.ProgressState(true)

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.autoAssignedComplainCount(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        //val message = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                        //viewState.value = ViewState.ShowMessage(message)
                        responseData.value = listOf()
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
        return responseData
    }


    fun assignedComplainCountDetails(requestBody: AutoAssignedCountDetailsRequest): LiveData<List<AutoAssignedCountDetailsResponse>> {

        val responseData: MutableLiveData<List<AutoAssignedCountDetailsResponse>> = MutableLiveData()
        viewState.value = ViewState.ProgressState(true)

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.autoAssignedComplainCountDetails(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        //val message = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
                        //viewState.value = ViewState.ShowMessage(message)
                        responseData.value = listOf()
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
        return responseData
    }


}