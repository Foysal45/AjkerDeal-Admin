package com.ajkerdeal.app.ajkerdealadmin.ui.retention_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportReqBody
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class RetentionReportViewModel(private val repository: AppRepository): ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    private val messageServerError = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    private val messageNetworkError = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    private val messageUnknownError = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    fun getRetentionMerchantFollowUpDetails(requestBody: RetentionReportReqBody) : LiveData<List<RetentionReportModel>> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<RetentionReportModel>>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getRetentionMerchantFollowUpDetails(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {

                            responseBody.value = response.body.model!!

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

    fun getRetentionMerchantFollowUpReportDetails(requestBody: RetentionReportDetailsReqBody): LiveData<List<RetentionReportDetailsModel>> {
        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<RetentionReportDetailsModel>>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getRetentionMerchantFollowUpReportDetails(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {

                            responseBody.value = response.body.model!!

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