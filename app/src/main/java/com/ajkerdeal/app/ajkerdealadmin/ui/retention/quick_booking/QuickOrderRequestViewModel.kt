package com.ajkerdeal.app.ajkerdealadmin.ui.retention.quick_booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.*
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.pickup_location.PickupLocation
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class QuickOrderRequestViewModel(private val repository: AppRepository) : ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    private val message = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"
    val serverErrorMessage = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    val networkErrorMessage = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    val unknownErrorMessage = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    val currentTimeSlot: LiveData<List<QuickOrderTimeSlotData>> by lazy {
        val calender = Calendar.getInstance()
        val selectedDate = sdf.format(calender.timeInMillis)
        getCollectionTimeSlot(TimeSlotRequest(selectedDate))
    }

    val upcomingTimeSlot: LiveData<List<QuickOrderTimeSlotData>> by lazy {
        val calender = Calendar.getInstance()
        calender.add(Calendar.DAY_OF_MONTH, 1)
        val selectedDate = sdf.format(calender.timeInMillis)
        getCollectionTimeSlot(TimeSlotRequest(selectedDate))
    }

    fun quickOrderRequest(requestBody: QuickOrderRequest): LiveData<QuickOrderRequestResponse> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<QuickOrderRequestResponse>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.quickOrderRequest(requestBody)
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

    private fun getCollectionTimeSlot(requestBody: TimeSlotRequest): LiveData<List<QuickOrderTimeSlotData>> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<List<QuickOrderTimeSlotData>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCollectionTimeSlot(requestBody)
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

    fun updateMultipleTimeSlot(requestBody: List<TimeSlotUpdateRequest>) : LiveData<Boolean>{

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateMultipleTimeSlot(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseData.value = response.body.model > 0
                        } else {
                            responseData.value = false
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

    fun deleteOrderRequest(orderRequestId: Int) : LiveData<Boolean>{

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<Boolean>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deleteOrderRequest(orderRequestId)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body.model != null) {
                            responseData.value = response.body.model > 0
                        } else {
                            responseData.value = false
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

    fun getPickupLocations(courierUserId: Int): LiveData<List<PickupLocation>> {

        viewState.value = ViewState.ProgressState(true)
        val responseData = MutableLiveData<List<PickupLocation>>()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getPickupLocations(courierUserId)
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