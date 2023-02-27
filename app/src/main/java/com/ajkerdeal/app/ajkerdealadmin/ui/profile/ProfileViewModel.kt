package com.ajkerdeal.app.ajkerdealadmin.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.PickUpLocationModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.quick_order.pickup_location.PickupLocation
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.haroldadmin.cnradapter.NetworkResponse
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File


class ProfileViewModel(private val repository: AppRepository): ViewModel() {

    val viewState = MutableLiveData<ViewState>(ViewState.NONE)
    private val messageServerError = "দুঃখিত, এই মুহূর্তে আমাদের সার্ভার কানেকশনে সমস্যা হচ্ছে, কিছুক্ষণ পর আবার চেষ্টা করুন"
    private val messageNetworkError = "দুঃখিত, এই মুহূর্তে আপনার ইন্টারনেট কানেকশনে সমস্যা হচ্ছে"
    private val messageUnknownError = "কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন"

    fun uploadProfilePhoto(context: Context, url: String): LiveData<Boolean> {

        val responseData: MutableLiveData<Boolean> = MutableLiveData()
        viewState.value = ViewState.ProgressState(true)
        val mediaTypeMultipart = "multipart/form-data".toMediaTypeOrNull()

        viewModelScope.launch(Dispatchers.IO) {

            val profileIdAD = SessionManager.userId
            val profileIdDT = SessionManager.dtUserId
            val file = File(url)
            val compressedFile = Compressor.compress(context, file)
            val requestFile = compressedFile.asRequestBody(mediaTypeMultipart)
            val part = MultipartBody.Part.createFormData("img", "${profileIdAD}.jpg", requestFile)
            val imageUrlAD = "images/admin_users/ad".toRequestBody()
            val imageUrlDT = "images/admin_users/dt".toRequestBody()
            val fileNameAD = "${profileIdAD}.jpg".toRequestBody()
            val fileNameDT = "${profileIdDT}.jpg".toRequestBody()
            val responseAD = repository.uploadProfileImage(imageUrlAD, fileNameAD, part)
            val responseDT = repository.uploadProfileImage(imageUrlDT, fileNameDT, part)

            Timber.d("admin_users_image ${"https://static.ajkerdeal.com/images/admin_users/ad/$profileIdAD.jpg"}")
            Timber.d("admin_users_image ${"https://static.ajkerdeal.com/images/admin_users/dt/$profileIdDT.jpg"}")

            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (responseDT) {
                    is NetworkResponse.Success -> {
                        if (responseDT.body != null) {
                            responseData.value = responseDT.body
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
                        Timber.d(responseDT.error)
                    }
                }
            }
        }
        return responseData
    }

    fun employeeInformationUpdate(requestBody: ProfileUpdateRequest): LiveData<ProfileUpdateResponse> {

        val responseData: MutableLiveData<ProfileUpdateResponse> = MutableLiveData()
        viewState.value = ViewState.ProgressState(true)

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.employeeInformationUpdate(requestBody)
            withContext(Dispatchers.Main) {
                viewState.value = ViewState.ProgressState(false)
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.body != null) {
                            responseData.value = response.body
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


}