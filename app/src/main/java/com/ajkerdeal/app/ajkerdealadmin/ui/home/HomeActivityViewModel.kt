package com.ajkerdeal.app.ajkerdealadmin.ui.home

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository

class HomeActivityViewModel(private val repository: AppRepository): ViewModel() {
    val currentLocation = MutableLiveData<Location?>(null)
    val isOfflineLive = MutableLiveData<Boolean>()
    val isGPS = MutableLiveData<Boolean>(false)
    val updateProfilePic = MutableLiveData<Boolean>(false)
    var addressFromGeoCoder = MutableLiveData<String>("")



}