package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.UpdateMerchantInformationRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.DistrictModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMerchantInformationUpdateBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.MerchantInfoViewModelList
import com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.location_selection_bottom_sheet.LocationSelectionBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.pick_up_location.PickUpLocationAdapter
import com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.pick_up_location.UpdatePickupLocationBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.retention_acquisition_bottomsheet.RetentionAcquisitionBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.ajkerdeal.app.ajkerdealadmin.utils.Validator.isValidEmail
import com.ajkerdeal.app.ajkerdealadmin.utils.Validator.isValidMobileNumber
import org.koin.android.ext.android.inject
import timber.log.Timber

class MerchantInformationUpdateFragment : Fragment() {

    private var binding: FragmentMerchantInformationUpdateBinding? = null
    private val viewModel: MerchantInfoViewModelList by inject()

    private val dataAdapter: PickUpLocationAdapter = PickUpLocationAdapter()

    private var districtLists: MutableList<DistrictModel> = mutableListOf()
    private var pickupDistrictLists: MutableList<DistrictModel> = mutableListOf()
    private var thanaLists: MutableList<DistrictModel> = mutableListOf()
    private var filteredDistrictLists: List<DistrictModel> = mutableListOf()

    lateinit var merchantInfoModel: CourierUser
    private var selectedDistrictId: Int = 0
    private var selectedThanaId: Int = 0

    //pickup location
    private var pickupSelectedDistrictID: Int = 0
    private var pickupDistrictNameBengali: String = ""
    private var pickupSelectedThana: Int = 0
    private var pickupThanaNameBengali: String = ""
    private var retentionID: Int = 0
    private var acquisitionID: Int = 0
    private var date: String = ""

    private var retentionUserList: MutableList<ADUsersModel> = mutableListOf()
    private var acquisitionUserList: MutableList<ADUsersModel> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentMerchantInformationUpdateBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initClickLister()
    }

    private fun initView() {

        merchantInfoModel = arguments?.getParcelable("merchantInfoModel") ?: CourierUser()
        val liveDate = merchantInfoModel.joinDate.split("T").first()
        date = DigitConverter.formatDate(liveDate, "yyyy-MM-dd", "yyyy-MM-dd")

        with(binding?.recyclerview!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
        }

        setData()
        setUpOfferTypeSpinner()
        setUpKnowingSourceSpinner()
        setUpPrioritySpinner()
        fetchAndInitializedAllDistrict()
        fetchAllRetentionAndAcquisitionManager()
        fetchPickupLocation()
    }

    private fun setData(){
        binding?.companyName?.setText(merchantInfoModel.companyName)
        binding?.merchantName?.setText(merchantInfoModel.userName)
        binding?.userPhoneNumber?.setText(merchantInfoModel.mobile)
        binding?.userALtPhoneNumber?.setText(merchantInfoModel.alterMobile)
        binding?.userBkashPhoneNumber?.setText(merchantInfoModel.bkashNumber)
        binding?.userEmail?.setText(merchantInfoModel.emailAddress)
        binding?.userPassword?.setText(merchantInfoModel.password)
        binding?.userAddress?.setText(merchantInfoModel.address)
        binding?.joinDate?.setText(DigitConverter.formatDate(date,"yyyy-MM-dd", "dd MMM"))
        binding?.returnCharge?.setText(merchantInfoModel.returnCharge.toString())
        binding?.offerBKashDiscountDhaka?.setText(merchantInfoModel.offerBkashDiscountDhaka.toString())
        binding?.offerBKashDiscountOutsideDhaka?.setText(merchantInfoModel.offerBkashDiscountOutSideDhaka.toString())
        binding?.maximumCODCharge?.setText(merchantInfoModel.maxCodCharge.toString())
        binding?.mailCharge?.setText(merchantInfoModel.mailCharge.toString())
        binding?.collectionCharge?.setText(merchantInfoModel.collectionCharge.toString())
        binding?.smscharge?.setText(merchantInfoModel.smsCharge.toString())
        binding?.creditCharge?.setText(merchantInfoModel.credit.toString())
        binding?.offerCODDiscount?.setText(merchantInfoModel.offerCodDiscount.toString())
        binding?.picklocation
        binding?.mobileNoPickUpLocation
        binding?.checkIsHeavyWeight?.isChecked = merchantInfoModel.isHeavyWeight
        binding?.checIsBreakAble?.isChecked = merchantInfoModel.isBreakAble
        binding?.checkQuickOrderActive?.isChecked = merchantInfoModel.isQuickOrderActive
        binding?.checkDocument?.isChecked = merchantInfoModel.isDocument
        binding?.checkAutoProcess?.isChecked = merchantInfoModel.isAutoProcess
        binding?.checkOfferActive?.isChecked = merchantInfoModel.isOfferActive
    }

    private fun initClickLister() {
        binding?.apply {
            updateBtn.setOnClickListener {
                if (validate()) {
                    updateMerchantInfo()
                }
            }

            userDistrict.setOnClickListener {
                fetchAllDistrict(0, isDistrict = true, isPicUp = false)
            }

            userThana.setOnClickListener {
                if (selectedDistrictId != 0) {
                    fetchAllDistrict(selectedDistrictId, isDistrict = false, isPicUp = false)
                } else {
                    context?.toast("Please Select District First")
                }
            }

            userRetentionManager.setOnClickListener {
                if (retentionUserList.isNotEmpty()) {
                    goToRetentionManagerSelectionDialogue(retentionUserList,true)
                } else {
                    context?.toast("No Items In Retention")
                }
            }

            userAcqusitionManager.setOnClickListener {
                if (acquisitionUserList.isNotEmpty()) {
                    goToRetentionManagerSelectionDialogue(acquisitionUserList, false)
                } else {
                    context?.toast("No Items In Acquisition")
                }
            }

            spinnerPickUpDistrict.setOnClickListener {
                fetchAllDistrict(0, isDistrict = true, isPicUp = true)
            }

            thanaSelect.setOnClickListener {
                if (pickupSelectedDistrictID != 0){
                    fetchAllDistrict(pickupSelectedDistrictID,  isDistrict = false, isPicUp = true)
                }else{
                    context?.toast("Please Select District First")
                }
            }

            addPicUpLocation.setOnClickListener {
                if (pickUpLocationValidation()) {
                    dataAdapter.addItem(
                        MerchantPickupLocation(

                        )
                    )
                    val requestBody = MerchantPickupLocation().apply {
                        districtId = pickupSelectedDistrictID
                        thanaId = pickupSelectedThana
                        courierUserId = merchantInfoModel.courierUserId
                        pickupAddress = binding?.picklocation?.text.toString()
                        districtName = pickupDistrictNameBengali
                        thanaName = pickupThanaNameBengali
                        latitude = ""
                        longitude = ""
                        mobile = binding?.mobileNoPickUpLocation?.text.toString()
                    }
                    viewModel.addPickupLocations(requestBody).observe(viewLifecycleOwner, Observer { model ->
                        //pickupAddressAdapter.addItem(requestBody)
                        viewModel.getPickupLocation(merchantInfoModel.courierUserId)
                        context?.toast("পিকআপ লোকেশন অ্যাড হয়েছে")

                        binding?.spinnerPickUpDistrict?.setText("")
                        binding?.thanaSelect?.setText("")
                        binding?.picklocation?.setText("")
                        binding?.mobileNoPickUpLocation?.setText("")
                    })
                }
            }
        }

        dataAdapter.onEditClicked = { model ->
            val tag = UpdatePickupLocationBottomSheet.tag
            val dialog = UpdatePickupLocationBottomSheet.newInstance(model)
            dialog.show(childFragmentManager, tag)
            dialog.onUpdateClicked = { model ->
                viewModel.updatePickupLocations(model).observe(viewLifecycleOwner, {

                })
                dialog.dismiss()
                context?.toast("সফলভাবে পিকআপ লোকেশন আপডেট হয়েছে")
            }
        }

        dataAdapter.onDeleteClicked = { model ->
            alert("নির্দেশনা", "পিকআপ লোকেশন ডিলিট করতে চান?", true, "হ্যাঁ, ডিলিট করবো", "ক্যানসেল") {
                if (it == AlertDialog.BUTTON_POSITIVE) {
                    viewModel.deletePickupLocations(model).observe(viewLifecycleOwner, Observer {
                        if (it) {
                            context?.toast("সফলভাবে ডিলিট হয়েছে")
//                            viewModel.getPickupLocations(SessionManager.courierUserId)
                        }
                    })
                }
            }.show()
        }
    }

    private fun updateMerchantInfo(){

        val requestBody = UpdateMerchantInformationRequest(
            merchantInfoModel.accountName,
            merchantInfoModel.accountNumber,
            merchantInfoModel.acquisitionUserId,
            binding?.userAddress?.text.toString(),
            merchantInfoModel.advancePayment.toInt(),
            merchantInfoModel.alterMobile,
            merchantInfoModel.bankName,
            binding?.userBkashPhoneNumber?.text.toString(),
            merchantInfoModel.blockReason,
            merchantInfoModel.branchName,
            merchantInfoModel.categoryId,
            binding?.collectionCharge?.text.toString().toDouble().toInt(),
            binding?.companyName?.text.toString(),
            merchantInfoModel.courierUserId,
            binding?.creditCharge?.text.toString().toDouble().toInt(),
            merchantInfoModel.customerSMSLimit,
            merchantInfoModel.customerVoiceSmsLimit,
            merchantInfoModel.deliveryRangeIdIOutside,
            merchantInfoModel.deliveryRangeIdInside,
            selectedDistrictId,
            binding?.userEmail?.text.toString(),
            merchantInfoModel.fburl,
            merchantInfoModel.firebaseToken,
            merchantInfoModel.gender,
            binding?.checkActive?.isChecked == true,
            binding?.checkAutoProcess?.isChecked == true,
            merchantInfoModel.isBlock,
            binding?.checIsBreakAble?.isChecked == true,
            binding?.checkEmail?.isChecked == true,
            binding?.checSMS?.isChecked == true,
            binding?.checkDocument?.isChecked == true,
            binding?.checkEmail?.isChecked == true,
            binding?.checkIsHeavyWeight?.isChecked == true,
            merchantInfoModel.isLoanActive,
            binding?.checkOfferActive?.isChecked == true,
            binding?.checkQuickOrderActive?.isChecked == true,
            binding?.checSMS?.isChecked == true,
            merchantInfoModel.isTeleSales,
            binding?.joinDate?.text.toString(),
            binding?.knowingSourceSpinner?.selectedItem.toString(),
            merchantInfoModel.loanCompany,
            binding?.mailCharge?.text.toString().toDouble().toInt(),
            binding?.maximumCODCharge?.text.toString().toDouble().toInt(),
            merchantInfoModel.merchantAssignActive,
            binding?.userPhoneNumber?.text.toString(),
            binding?.offerBKashDiscountDhaka?.text.toString().toDouble().toInt(),
            binding?.offerBKashDiscountOutsideDhaka?.text.toString().toDouble().toInt(),
            binding?.offerCODDiscount?.text.toString().toDouble().toInt(),
            binding?.offerTypeSpinner?.selectedItemPosition ?: 0,
            merchantInfoModel.orderType,
            binding?.userPassword?.text.toString(),
            merchantInfoModel.preferredPaymentCycle,
            merchantInfoModel?.preferredPaymentCycleDate ?: "",
            binding?.prioritySpinner?.selectedItem.toString(),
            merchantInfoModel.refereeEndTime ?: "",
            merchantInfoModel.refereeOrder,
            merchantInfoModel.refereeStartTime ?: "",
            merchantInfoModel.referrer,
            merchantInfoModel.referrerEndTime ?: "",
            merchantInfoModel.referrerIsActive,
            merchantInfoModel.referrerOrder,
            merchantInfoModel.referrerStartTime ?: "",
            merchantInfoModel.refreshtoken,
            merchantInfoModel.registrationFrom,
            merchantInfoModel.remarks,
            retentionID,
            binding?.returnCharge?.text.toString().toDouble().toInt(),
            merchantInfoModel.routingNumber,
            binding?.smscharge?.text.toString().toDouble()?.toInt(),
            merchantInfoModel.sourceType,
            merchantInfoModel.subCategoryId,
            merchantInfoModel.teleSales,
            merchantInfoModel.teleSalesDate,
            selectedThanaId,
            binding?.merchantName?.text.toString(),
            merchantInfoModel.webURL,
            merchantInfoModel.weightRangeId
        )
        viewModel.updateMerchantInformation(requestBody).observe(viewLifecycleOwner, {
            if (it.courierUserId != 0){
                context?.toast("Updated Successfully")
                findNavController().popBackStack()
            }else{
                context?.toast("Something Went Wrong")
            }
        })

    }

    private fun validate(): Boolean {
        hideKeyboard()
        val merchantName = binding?.merchantName?.text.toString()
        if (merchantName.isEmpty()) {
            context?.toast("Enter merchant Name")
            binding?.merchantName?.requestFocus()
            return false
        }

        val bKashNumber = binding?.userBkashPhoneNumber?.text.toString()
        if (bKashNumber.isEmpty()) {
            context?.toast("Enter BKash Number")
            binding?.userBkashPhoneNumber?.requestFocus()
            return false
        }
        if (!isValidMobileNumber(bKashNumber)) {
            context?.toast("Enter Valid BKash Number")
            binding?.userBkashPhoneNumber?.requestFocus()
            return false
        }
        val email = binding?.userEmail?.text.toString()
        if (email.isEmpty()) {
            context?.toast("Enter Your Email")
            binding?.userEmail?.requestFocus()
            return false
        }
        if (!isValidEmail(email) ) {
            context?.toast("Enter Valid Email")
            binding?.userEmail?.requestFocus()
            return false
        }
        val maxCodCharge = binding?.maximumCODCharge?.text.toString()
        if (maxCodCharge.isEmpty()) {
            context?.toast("Enter maximum COD charge")
            binding?.maximumCODCharge?.requestFocus()
            return false
        }
        val collectionCharge = binding?.collectionCharge?.text.toString()
        if (collectionCharge.isEmpty()) {
            context?.toast("Enter Collection Charge")
            binding?.collectionCharge?.requestFocus()
            return false
        }
        val offerCODDiscount = binding?.offerCODDiscount?.text.toString()
        if (offerCODDiscount.isEmpty()) {
            context?.toast("Enter offer COD discount")
            binding?.offerCODDiscount?.requestFocus()
            return false
        }
        val offerBkashDiscount = binding?.offerBKashDiscountDhaka?.text.toString()
        if (offerBkashDiscount.isEmpty()) {
            context?.toast("Enter BKASH discount")
            binding?.offerBKashDiscountDhaka?.requestFocus()
            return false
        }
        val offerBkashDiscountOutSideDhaka = binding?.offerBKashDiscountOutsideDhaka?.text.toString()
        if (offerBkashDiscountOutSideDhaka.isEmpty()) {
            context?.toast("Enter BKASH discount outside Dhaka")
            binding?.offerBKashDiscountOutsideDhaka?.requestFocus()
            return false
        }
        val userSelectedDistrict = binding?.userDistrict?.text.toString()
        if (userSelectedDistrict.isEmpty()) {
            context?.toast("Enter User District")
            binding?.userDistrict?.requestFocus()
            return false
        }
        val userSelectedThana = binding?.userThana?.text.toString()
        if (userSelectedThana.isEmpty()) {
            context?.toast("Enter user thana")
            binding?.userThana?.requestFocus()
            return false
        }
        val userInputAddress = binding?.userAddress?.text.toString()
        if (userInputAddress.isEmpty()) {
            context?.toast("Enter address")
            binding?.userAddress?.requestFocus()
            return false
        }
        val selectedRetentionManager = binding?.userRetentionManager?.text.toString()
        if (selectedRetentionManager.isEmpty()) {
            context?.toast("Enter user retention")
            binding?.userRetentionManager?.requestFocus()
            return false
        }
        val selectedAcquisitionManager = binding?.userAcqusitionManager?.text.toString()
        if (selectedAcquisitionManager.isEmpty()) {
            context?.toast("Enter acquisition manager")
            binding?.userAcqusitionManager?.requestFocus()
            return false
        }
        val enteredMaximumCODCharge = binding?.maximumCODCharge?.text?.toString()
        if (enteredMaximumCODCharge?.isEmpty()!!) {
            context?.toast("Enter maximum COD charge")
            binding?.maximumCODCharge?.requestFocus()
            return false
        }
        val enteredCollectionCharge = binding?.collectionCharge?.text?.toString()
        if (enteredCollectionCharge?.isEmpty()!!) {
            context?.toast("Enter Collection charge")
            binding?.collectionCharge?.requestFocus()
            return false
        }
        val enteredOfferCODDiscount = binding?.offerCODDiscount?.text.toString()
        if (enteredOfferCODDiscount.isEmpty()) {
            context?.toast("Enter offer COD discount")
            binding?.offerCODDiscount?.requestFocus()
            return false
        }
        val enteredOfferBKashDiscountDhaka = binding?.offerBKashDiscountDhaka?.text.toString()
        if (enteredOfferBKashDiscountDhaka.isEmpty()) {
            context?.toast("Enter offer bkash discout dhaka")
            binding?.offerBKashDiscountDhaka?.requestFocus()
            return false
        }
        val enteredOfferBKashDiscountOutsideDhaka = binding?.offerBKashDiscountOutsideDhaka?.text.toString()
        if (enteredOfferBKashDiscountOutsideDhaka.isEmpty()) {
            context?.toast("Enter offer bkash discout outside Dhaka")
            binding?.offerBKashDiscountOutsideDhaka?.requestFocus()
            return false
        }

        return true
    }

    private fun goToLocationSelectionBottomSheet(districtDataModel: List<DistrictModel>, isDistrict: Boolean, isPicUp: Boolean) {

        val dialog = LocationSelectionBottomSheet.newInstance(districtDataModel as MutableList<DistrictModel>)
        dialog.show(childFragmentManager, LocationSelectionBottomSheet.tag)
        dialog.onLocationPicked = { districtModel ->
            if (isDistrict){
                if (isPicUp){
                    pickupSelectedDistrictID = districtModel.districtId
                    pickupDistrictNameBengali = districtModel.districtBng
                    binding?.spinnerPickUpDistrict?.setText(districtModel.district)
                    pickupSelectedThana = 0
                    binding?.thanaSelect?.setText("")
                }else{
                    selectedDistrictId = districtModel.districtId
                    binding?.userDistrict?.setText(districtModel.district)
                    selectedThanaId = 0
                    binding?.userThana?.setText("")
                }
            }else{
                if (isPicUp){
                    pickupSelectedThana = districtModel.districtId
                    pickupThanaNameBengali = districtModel.districtBng
                    binding?.thanaSelect?.setText(districtModel.district)
                }else{
                    selectedThanaId = districtModel.districtId
                    binding?.userThana?.setText(districtModel.district)
                }
            }
        }
    }

    private fun goToRetentionManagerSelectionDialogue(retentionList: List<ADUsersModel>, isRetention: Boolean){

        val dialog = RetentionAcquisitionBottomSheet.newInstance(retentionList)
        dialog.show(childFragmentManager, RetentionAcquisitionBottomSheet.tag)
        dialog.onLocationPicked = { retentionModel ->
            if (isRetention){
                retentionID = retentionModel.userId
                binding?.userRetentionManager?.setText(retentionModel.fullName)
            }else{
                acquisitionID = retentionModel.userId
                binding?.userAcqusitionManager?.setText(retentionModel.fullName)
            }
        }

    }

    private fun fetchAndInitializedAllDistrict() {
        viewModel.loadAllDistrictsById(0).observe(viewLifecycleOwner, Observer { list ->
            districtLists.clear()
            districtLists.addAll(list)
            setMerchantDistrictData()
        })
        viewModel.loadAllDistrictsById(merchantInfoModel.districtId).observe(viewLifecycleOwner, Observer { list ->
            thanaLists.clear()
            thanaLists.addAll(list)
            setMerchantThanaData()
        })
    }

    private fun fetchAllDistrict(id: Int, isDistrict: Boolean, isPicUp: Boolean) {

        viewModel.loadAllDistrictsById(id).observe(viewLifecycleOwner, Observer { list ->
            districtLists.clear()
            districtLists.addAll(list)
            pickupDistrictLists.clear()
            val picUpLocation = list.filter { it.isPickupLocation }
            pickupDistrictLists.addAll(picUpLocation)
            if (!isPicUp){
                goToLocationSelectionBottomSheet(districtLists, isDistrict, isPicUp)
            }else{
                if (isDistrict){
                    goToLocationSelectionBottomSheet(pickupDistrictLists, isDistrict, isPicUp)
                }else{
                    goToLocationSelectionBottomSheet(districtLists, isDistrict, isPicUp)
                }

            }
        })

    }

    private fun fetchAllRetentionAndAcquisitionManager() {
        viewModel.getUserInfo().observe(viewLifecycleOwner, { user ->
            if (!user.isNullOrEmpty()){
                val filteredRetention = user.filter { it.isRetention }
                if (!filteredRetention.isNullOrEmpty()){
                    retentionUserList.clear()
                    retentionUserList.addAll(user.filter { it.isRetention })
                    setRetentionManagerData()
                }

                val filteredAcquisition = user.filter { it.isRetention }
                if (!filteredAcquisition.isNullOrEmpty()){
                    acquisitionUserList.clear()
                    acquisitionUserList.addAll(user.filter { !it.isRetention })
                    setAcqusitionManagerData()
                }
            }

        })
    }

    private fun fetchPickupLocation() {
        viewModel.getPickupLocation(merchantInfoModel.courierUserId).observe(viewLifecycleOwner, { pickUpLocationModelList ->
            if (pickUpLocationModelList.isEmpty()) {
                binding?.emptyView?.visibility = View.VISIBLE
            } else {
                dataAdapter.initList(pickUpLocationModelList)
                binding?.emptyView?.visibility = View.GONE
            }
        })
    }

    private fun setUpOfferTypeSpinner() {
        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        pickupBreakReasonList.add("All")
        pickupBreakReasonList.add("COD")
        pickupBreakReasonList.add("BKash")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBreakReasonList)
        binding?.offerTypeSpinner?.adapter = spinnerAdapter
        binding?.offerTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
    }

    private fun setUpKnowingSourceSpinner() {
        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        pickupBreakReasonList.add("Others")
        pickupBreakReasonList.add("Facebook")
        pickupBreakReasonList.add("Email")
        pickupBreakReasonList.add("LinkedIN")
        pickupBreakReasonList.add("Twitter")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBreakReasonList)
        binding?.knowingSourceSpinner?.adapter = spinnerAdapter
        binding?.knowingSourceSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
    }

    private fun setUpPrioritySpinner() {
        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        pickupBreakReasonList.add("High")
        pickupBreakReasonList.add("Medium")
        pickupBreakReasonList.add("Low")


        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBreakReasonList)
        binding?.prioritySpinner?.adapter = spinnerAdapter
        binding?.prioritySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
    }

    private fun setMerchantDistrictData(){

        if (merchantInfoModel.districtId != 0){
            val filteredDistrictList = districtLists.find { it.districtId == merchantInfoModel.districtId } ?: DistrictModel()
            selectedDistrictId = merchantInfoModel.districtId
            binding?.userDistrict?.setText(
                if (filteredDistrictList.district.isNotEmpty()) {
                    filteredDistrictList.district
                } else {
                    ""
                }
            )
        }

    }

    private fun setMerchantThanaData(){
        Timber.d("debugThana ${thanaLists}")
        if (merchantInfoModel.thanaId != 0){
            val filteredThanaList = thanaLists.find { it.districtId == merchantInfoModel.thanaId } ?: DistrictModel()
            selectedThanaId = merchantInfoModel.thanaId
            Timber.d("debugThana $filteredThanaList")
            binding?.userThana?.setText(
                if (filteredThanaList.district.isNotEmpty()) {
                    filteredThanaList.district
                } else {
                    ""
                }
            )
        }

    }

    private fun setRetentionManagerData(){
        if (merchantInfoModel.retentionUserId != 0){
            val filteredList = retentionUserList.find { it.userId == merchantInfoModel.retentionUserId}
            retentionID = merchantInfoModel.retentionUserId
            binding?.userRetentionManager?.setText(filteredList?.fullName)
            binding?.userRetentionManager?.setText(
                if (filteredList?.fullName?.isNotEmpty() == true) {
                    filteredList?.fullName
                } else {
                    ""
                }
            )
        }

    }

    private fun setAcqusitionManagerData(){
        if (merchantInfoModel.acquisitionUserId != 0){
            val filteredList = retentionUserList.find { it.userId == merchantInfoModel.acquisitionUserId}
            acquisitionID = merchantInfoModel.acquisitionUserId
            binding?.userAcqusitionManager?.setText(filteredList?.fullName)
            binding?.userAcqusitionManager?.setText(
                if (filteredList?.fullName?.isNotEmpty() == true) {
                    filteredList?.fullName
                } else {
                    ""
                }
            )
        }

    }

    private fun pickUpLocationValidation(): Boolean {
        val pickUpLocationDistirct = binding?.spinnerPickUpDistrict?.text.toString().trim()
        if (pickUpLocationDistirct.isEmpty()) {
            context?.toast("Enter  District")
            binding?.spinnerPickUpDistrict?.requestFocus()
            return false
        }
        val pickUpLocationThana = binding?.thanaSelect?.text.toString().trim()
        if (pickUpLocationThana.isEmpty()) {
            context?.toast("Enter thana")
            binding?.thanaSelect?.requestFocus()
            return false
        }
        val picklocation = binding?.picklocation?.text.toString().trim()
        if (picklocation.isEmpty()) {
            context?.toast("Enter pickup location")
            binding?.picklocation?.requestFocus()
            return false
        }
        val mobileNoPickUpLocation = binding?.mobileNoPickUpLocation?.text.toString().trim()
        if (mobileNoPickUpLocation.isEmpty()) {
            context?.toast("Enter mobile number")
            binding?.mobileNoPickUpLocation?.requestFocus()
            return false
        }
        if (!isValidMobileNumber(mobileNoPickUpLocation)) {
            context?.toast("Enter Valid mobile number")
            binding?.mobileNoPickUpLocation?.requestFocus()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
