package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.PagingModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.LeaveReportResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.MerchantInfoUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.MerchantUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.UpdateMerchantInformationRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.DistrictModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.PickUpLocationModel
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.exhaustive
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext
import timber.log.Timber


class MerchantInfoViewModelList(val repository: AppRepository) : ViewModel() {
    val viewState = MutableLiveData<ViewState>(ViewState.NONE)

    val pagingState: MutableLiveData<PagingModel<List<CourierUser>>> = MutableLiveData()
    val UserData: MutableLiveData<List<ADUsersModel>> = MutableLiveData()
    val pickupLocation: MutableLiveData<List<PickUpLocationModel>> = MutableLiveData()

    private val messageServerError = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    private val messageNetworkError = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    private val messageUnknownError = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    fun getSrWiseCourierUsersInfo(requestBody: MerchantUpdateRequest, index: Int) {

        viewState.value = ViewState.ProgressState(true)

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getSrWiseCourierUsersInfo(requestBody)

            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (index == 0) {
                            if (response.body.model.courierUsers.isNotEmpty()) {
                                pagingState.value = PagingModel(true, response.body.model.courierUsers.count(), 0, 0.0, 0.0, response.body.model.courierUsers)
                            } else {
                                pagingState.value = PagingModel(true, 0, 0, 0.0, 0.0, response.body.model.courierUsers)
                            }
                        } else {
                            pagingState.value = PagingModel(false, 0, 0, 0.0, 0.0, response.body.model.courierUsers)
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
    }

    fun loadAllDistrictsById(id: Int): LiveData<List<DistrictModel>> {

        viewState.value = ViewState.ProgressState(true)
        val responseBody = MutableLiveData<List<DistrictModel>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.loadAllDistrictsById(id)
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

    fun getUserInfo(): LiveData<List<ADUsersModel>> {
        viewState.value = ViewState.ProgressState(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAdUsers()

            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        UserData.value = response.body.model
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
        return UserData
    }

    fun getPickupLocation(userId: Int): LiveData<List<MerchantPickupLocation>> {
        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<List<MerchantPickupLocation>>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getPickupLocation(userId)

            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        responseData.value = response.body.model!!
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

    fun deletePickupLocations(requestBody: MerchantPickupLocation): LiveData<Boolean> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deletePickupLocations(requestBody.id)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseData.value = response.body.model == 1
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

    fun addPickupLocations(requestBody: MerchantPickupLocation): LiveData<MerchantPickupLocation> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<MerchantPickupLocation>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.addPickupLocations(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseData.value = response.body.model!!
                        }
                    }
                    is NetworkResponse.ServerError -> {
                        val message = "পিকআপ লোকেশন ইতিমধ্যে অ্যাড করেছেন"
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

    fun updatePickupLocations(requestBody: MerchantPickupLocation): LiveData<MerchantPickupLocation> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<MerchantPickupLocation>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updatePickupLocations(requestBody.id, requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseData.value = response.body.model!!
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

    fun updateMerchantInformation(requestBody: UpdateMerchantInformationRequest): LiveData<MerchantInfoUpdateResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<MerchantInfoUpdateResponse>()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateMerchantInformation(requestBody.courierUserId, requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseData.value = response.body.model!!
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

}