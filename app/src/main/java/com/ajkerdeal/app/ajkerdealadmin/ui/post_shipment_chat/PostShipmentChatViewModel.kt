package com.ajkerdeal.app.ajkerdealadmin.ui.post_shipment_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.PagingModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUsersInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.exhaustive
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class PostShipmentChatViewModel(private val repository: AppRepository) : ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    val pagingState: MutableLiveData<PagingModel<List<CourierUser>>> = MutableLiveData()

    private val messageServerError = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    private val messageNetworkError = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    private val messageUnknownError = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    fun getCourierUsersInfo(requestBody: CourierUsersInfoRequest, index: Int): LiveData<CourierUser> {

        val responseData: MutableLiveData<CourierUser> = MutableLiveData()
        viewState.value = ViewState.ProgressState(true)

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCourierUsersInfo(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (index == 0) {
                            if (response.body.model.courierUsers?.isNotEmpty()!!) {
                                pagingState.value = PagingModel(true, response.body.model.courierUsers?.size!!, 0, 0.0, 0.0, response.body.model.courierUsers!!)
                            } else {
                                pagingState.value = PagingModel(true, 0, 0, 0.0, 0.0, response.body.model.courierUsers!!)
                            }
                        } else {
                            pagingState.value = PagingModel(false, 0, 0, 0.0, 0.0, response.body.model.courierUsers!!)
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
                }.exhaustive
            }
        }
        return responseData
    }

}