package com.ajkerdeal.app.ajkerdealadmin.ui.instant_payment_information

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPaymentModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPymentRequest
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class InstantPaymentViewModel(private val repository: AppRepository) : ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)

    fun getInstantPaymentInformationList(requestBody: InstantPymentRequest): LiveData<List<InstantPaymentModel>> {
        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<InstantPaymentModel>>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getInstantPaymentInformation(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        responseBody.value = response.body.model!!
                        Log.d("TAG", "response: " + response)
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

}
