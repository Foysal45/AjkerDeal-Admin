package com.ajkerdeal.app.ajkerdealadmin.ui.retention

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.PagingModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info.CalledInfoData
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info.CalledInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info.VisitedInfoData
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info.VisitedInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.bd.deliverytiger.app.api.model.config.BannerResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class RetentionMerchantListViewModel(private val repository: AppRepository): ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    val pagingState: MutableLiveData<PagingModel<List<RetentionMerchentListModel>>> = MutableLiveData()

    val bannerInfo = MutableLiveData<BannerResponse>()

    fun getRetentionMerchantList(requestBody: RetentionMerchantListRequest){

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<RetentionMerchentListModel>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getRetentionMerchantList(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (requestBody.index == 0){
                            if (response.body.model.isNotEmpty()){
                                pagingState.value = PagingModel(true, response.body.model.count(), 0, 0.0, 0.0, response.body.model)
                            }else{
                                pagingState.value = PagingModel(true, 0, 0, 0.0, 0.0, response.body.model)

                            }
                        }else{
                            pagingState.value = PagingModel(false, 0, 0, 0.0, 0.0, response.body.model)
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

       // return responseBody
    }

    fun getOrderWiseRetentionMerchantList(requestBody: OrderWiseRetentionMerchantListRequest){

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<RetentionMerchentListModel>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getOrderWiseRetentionMerchantList(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (requestBody.index == 0){
                            if (response.body.model.isNotEmpty()){
                                pagingState.value = PagingModel(true, response.body.model.count(), 0, 0.0, 0.0, response.body.model)
                            }else{
                                pagingState.value = PagingModel(true, 0, 0, 0.0, 0.0, response.body.model)

                            }
                        }else{
                            pagingState.value = PagingModel(false, 0, 0, 0.0, 0.0, response.body.model)
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

       // return responseBody
    }

    fun updateVisitedMerchant(requestBody: VisitedInfoRequest): LiveData<VisitedInfoData> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<VisitedInfoData>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateVisitedMerchant(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body.model!!
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

    fun updateCalledMerchant(requestBody: CalledInfoRequest): LiveData<CalledInfoData> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<CalledInfoData>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateCalledMerchant(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseBody.value = response.body.model!!
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

    fun getOrderCount(requestBody: OrderCountReqBody): LiveData<MutableList<OrderCountResponse>> {

        //viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<MutableList<OrderCountResponse>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getOrderCount(requestBody)
            withContext(Dispatchers.Main) {
                //viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseBody.value = response.body.model as MutableList<OrderCountResponse>
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

    fun getDataDuration(): LiveData<BannerResponse> {

        //viewState.value = ViewState.ProgressState(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getDataDuration()
            withContext(Dispatchers.Main) {
                //viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            bannerInfo.value = response.body.model!!
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
        return bannerInfo
    }

}