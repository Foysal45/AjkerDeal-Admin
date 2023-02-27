package com.ajkerdeal.app.ajkerdealadmin.ui.profile

import com.ajkerdeal.app.ajkerdealadmin.api.models.profile.ProfileUpdateRequest
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentProfileBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeActivityViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.login.LoginFragment
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import org.koin.android.ext.android.inject
import timber.log.Timber

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by inject()
    private val homeViewModel: HomeActivityViewModel by inject()

    private var profileImgUrl: String = ""
    private var selectedGenderType = 0
    private var selectedGender = ""
    private var selectedBloodGroupType = 0
    private var selectedBloodGroup = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentProfileBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment().apply {}
        val tag: String = LoginFragment::class.java.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBloodGroupSpinner()
        setUpGenderSpinner()
        initClickLister()
        loadProfileImage()
        fetchProfileData()

    }

    private fun initClickLister() {
        binding?.profilePicEdit?.setOnClickListener {
            pickUpImage()
        }
        binding?.updateBtn?.setOnClickListener {
            if (validate()){
                updateProfileData()
            }
        }
    }

    private fun updateProfileData(){

        val requestBody = ProfileUpdateRequest(
            SessionManager.userId.toString(),
            binding?.userName?.text?.trim().toString(),
            selectedGender,
            binding?.userAddress?.text?.trim().toString(),
            binding?.userPhoneNumber?.text?.trim().toString(),
            selectedBloodGroup,
            binding?.userEmail?.text?.trim().toString(),
        )
        viewModel.employeeInformationUpdate(requestBody).observe(viewLifecycleOwner, Observer { response->
            if (response.status){
                context?.toast("Updated Successfully")
                sessionUpdate()
            }
        })
        if (profileImgUrl.isNotEmpty()){
            viewModel.uploadProfilePhoto(requireContext(), profileImgUrl).observe(viewLifecycleOwner, Observer {
                if (it){
                    context?.toast("image updated")
                    SessionManager.profileImage = "https://static.ajkerdeal.com/images/admin_users/dt/${SessionManager.dtUserId}.jpg"
                    homeViewModel.updateProfilePic.value = true
                }
            })
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.ShowMessage -> {
                    requireContext().toast(state.message)
                }
                is ViewState.KeyboardState -> {
                    hideKeyboard()
                }
                is ViewState.ProgressState -> {
                    if (state.isShow) {
                        binding?.progressBar?.visibility = View.VISIBLE
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun validate(): Boolean {

        val userName = binding?.userName?.text.toString()
        val userMobileNumber = binding?.userPhoneNumber?.text.toString()
        val userEmailAddress = binding?.userEmail?.text.toString()
        val userAddress = binding?.userAddress?.text.toString()

        if (userName.trim().isEmpty()) {
            context?.toast("Please, Write Name")
            return false
        }
        if (userMobileNumber.trim().isEmpty()) {
            context?.toast("Please, Write Mobile Number")
            return false
        }
        if (!Validator.isValidMobileNumber(binding?.userPhoneNumber?.text.toString()) || userMobileNumber.length < 11) {
            context?.toast("Please, Write Correct Mobile Number")
            return false
        }
        if (userEmailAddress.trim().isEmpty()) {
            context?.toast("Please, Write Email Address")
            return false
        }
        if (!Validator.isValidEmail(binding?.userEmail?.text.toString())){
            context?.toast("Please, Write Correct Email Address")
            return false
        }
        if (userAddress.trim().isEmpty()) {
            context?.toast("Please Give User Address")
            return false
        }

        return true
    }

    private fun setUpGenderSpinner() {

        val pickupGenderList: MutableList<String> = mutableListOf()
        pickupGenderList.add(
            if (SessionManager.gender.isEmpty()){
                "Select Gender"
            }else{
                SessionManager.gender
            }
        )
        pickupGenderList.add("Male")
        pickupGenderList.add("Female")
        pickupGenderList.add("Other")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupGenderList)
        binding?.spinnerGender?.adapter = spinnerAdapter
        binding?.spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGenderType = position
                selectedGender = pickupGenderList[position]
            }
        }

    }

    private fun setUpBloodGroupSpinner() {

        val pickupBloodGroupList: MutableList<String> = mutableListOf()
        pickupBloodGroupList.add(
            if (SessionManager.blood.isEmpty()){
                "Select Blood Group"
            }else{
                SessionManager.blood
            }
        )
        pickupBloodGroupList.add("A+")
        pickupBloodGroupList.add("A-")
        pickupBloodGroupList.add("B+")
        pickupBloodGroupList.add("B-")
        pickupBloodGroupList.add("AB+")
        pickupBloodGroupList.add("AB-")
        pickupBloodGroupList.add("O-")
        pickupBloodGroupList.add("O+")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBloodGroupList)
        binding?.spinnerBloodGroup?.adapter = spinnerAdapter
        binding?.spinnerBloodGroup?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedBloodGroupType = position
                selectedBloodGroup = pickupBloodGroupList[position]
            }
        }

    }

    private fun loadProfileImage(){
        if (SessionManager.profileImage.isNotEmpty()) {
            binding?.profilePic?.let { view ->
                Glide.with(view)
                    .load(SessionManager.profileImage)
                    .apply(RequestOptions().placeholder(R.drawable.ic_person_circle).error(R.drawable.ic_person_circle).circleCrop())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(view)
            }
        }
    }

    private fun pickUpImage() {

        if (!isStoragePermissions()) {
            return
        }
        try {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
            }.also {
                getImages.launch(it)
            }
        } catch (e: Exception) {
            Timber.d(e)
            context?.toast("No Application found to handle this action")
        }
    }

    private fun isStoragePermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                val storagePermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (storagePermissionRationale) {
                    permission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    permission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                false
            } else {
                true
            }
        } else {
            return true
        }
    }

    private val getImages = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fileUri = result?.data?.data
            Timber.d("FileUri: $fileUri")
            val actualPath = FileUtils(requireContext()).getPath(fileUri)
            Timber.d("FilePath: $actualPath")
            profileImgUrl = actualPath

            binding?.profilePic?.let { view ->
                Glide.with(view)
                    .load(profileImgUrl)
                    .apply(RequestOptions().placeholder(R.drawable.ic_person_circle).error(R.drawable.ic_person_circle).circleCrop())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .into(view)
            }
        }

    }

    private val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { hasPermission ->
        if (hasPermission) {

        } else {
            val storagePermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (storagePermissionRationale) {
                alert("Permission Required", "App required Storage permission to function properly. Please grand permission.", true, "Give Permission", "Cancel") {
                    if (it == AlertDialog.BUTTON_POSITIVE) {
                        isStoragePermissions()
                    }
                }.show()
            } else {
                alert("Permission Required", "Please go to Settings to enable Storage permission. (Settings-apps--permissions)", true, "Settings", "Cancel") {
                    if (it == AlertDialog.BUTTON_POSITIVE) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${requireContext().packageName}")).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                }.show()
            }
        }
    }

    private fun fetchProfileData() {
        binding?.userName?.setText(SessionManager.userFullName)
        binding?.userPhoneNumber?.setText(SessionManager.mobile)
        binding?.userEmail?.setText(SessionManager.email)
        binding?.userAddress?.setText(SessionManager.address)
    }


    private fun sessionUpdate(){

        val name = binding?.userName?.text?.trim().toString()
        val mobile = binding?.userPhoneNumber?.text?.trim().toString()
        val email = binding?.userEmail?.text?.trim().toString()
        val address = binding?.userAddress?.text?.trim().toString()

        if (name.isNotEmpty()){
            SessionManager.userFullName = name
        }
        if (mobile.isNotEmpty()){
            SessionManager.mobile = mobile
        }
        if (email.isNotEmpty()){
            SessionManager.email = email
        }
        if (selectedGenderType != 0){
            SessionManager.gender = selectedGender
        }
        if (selectedBloodGroupType != 0){
            SessionManager.blood = selectedBloodGroup
        }
        if (address.isNotEmpty()){
            SessionManager.address = address
        }

    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}