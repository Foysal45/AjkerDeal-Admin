package com.ajkerdeal.app.ajkerdealadmin.ui.employee_leave

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMDataModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMNotification
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveSessionRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLeaveApplicationFormBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.Validator
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale


class LeaveApplicationFormFragment : Fragment() {
    private var binding: FragmentLeaveApplicationFormBinding? = null
    private val viewModel: LeaveApplicationViewModel by inject()
    private val viewModelSendPushNotification: ChatComposeViewModel by inject()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM, yyyy", Locale.US)
    private var firebaseWebApiKey: String? = null

    private var selectedDate = ""
    private var fromDate = ""
    private var toDate = ""
    private var selectedLeaveType = 0
    private var selectedDeptType = 0
    private var selectedDept = ""

    companion object {
        fun newInstance(): LeaveApplicationFormFragment = LeaveApplicationFormFragment().apply {}
        val tag: String = LeaveApplicationFormFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentLeaveApplicationFormBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initProfileInfo()
        initClickLister()
        setUpeDepartmentSpinner()
        setUpLeaveTypeSpinner()
        firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
    }

    private fun initClickLister(){
        binding?.applyBtn?.setOnClickListener {
            applyForLeave()
        }
        binding?.dateRangePicker?.setOnClickListener {
            dateRangePicker()
        }
        binding?.datePicker?.setOnClickListener {
            datePicker()
        }
    }

    private fun initProfileInfo(){
        binding?.etEmployeeName?.setText(SessionManager.userFullName)
        binding?.etEmployeeMobileNo?.setText(SessionManager.mobile)
    }

    private fun applyForLeave(){
        if (!validate()){
            return
        }
        val requestBody  = LeaveSessionRequest(
            SessionManager.userId.toString(),
            SessionManager.adminType,
            SessionManager.userName,
            binding?.etEmployeeName?.text?.trim().toString(),
            binding?.othersLeaveType?.text.toString(),
            selectedDept,
            fromDate,
            toDate,
            binding?.leaveReasonTV?.text?.trim().toString(),
            selectedDate,
            binding?.etEmployeeMobileNo?.text?.trim().toString(),
            "Pending")
        viewModel.startEmployeeLeaveSession(requestBody).observe(viewLifecycleOwner,Observer { response->
            if (response.status){
                sendPushNotification(userId = 67)
                sendPushNotification(userId = 68)
                context?.toast("Application submitted successfully")
            }else{
                context?.toast("Failed, Please try again")
            }
        })

    }
    private fun sendPushNotification(userId: Int ) {
        viewModel.userfirebasetoken(userId).observe(viewLifecycleOwner, Observer{ token ->
            Timber.d("${token} some token")
            Timber.d("some token $firebaseWebApiKey")
            if (!(token == null || firebaseWebApiKey.isNullOrEmpty())) {
                val title = "Leave Application"
                val body = "Type:${binding?.othersLeaveType?.text.toString()} \n" +
                        "Name: ${binding?.etEmployeeName?.text?.trim().toString()} \n" +
                        "Dept: $selectedDept \n" +
                        "Reason: ${binding?.leaveReasonTV?.text?.trim().toString()}"

                val notificationModel = FCMNotification(
                    title, body, "", ""
                )
                val requestBody = FCMRequest(token.firebasetoken!!, notificationModel, data = FCMDataModel(
                    notificationType = "Leave",
                    title = title, body = body, receiverId = userId.toString()))
                viewModelSendPushNotification.sendPushNotifications(firebaseWebApiKey!!, requestBody)
                findNavController().popBackStack()
            }
        })
    }


    private fun validate(): Boolean {

        val employeeName = binding?.etEmployeeName?.text.toString()
        val mobileNumber = binding?.etEmployeeMobileNo?.text.toString()
        val leaveReason = binding?.leaveReasonTV?.text.toString()

        if (selectedLeaveType == 0) {
            context?.toast("Please, Select Leave Type")
            return false
        }
        if (selectedDeptType == 0) {
            context?.toast("Please, Select Department")
            return false
        }
        if (fromDate.trim().isEmpty()) {
            context?.toast("Please, Select Leave Date Ranges")
            return false
        }
        if (selectedDate.trim().isEmpty()) {
            context?.toast("Please, Select Resumption of Duty's Date")
            return false
        }
        if (employeeName.trim().isEmpty()) {
            context?.toast("Please, Write Name")
            return false
        }
        if (mobileNumber.trim().isEmpty()) {
            context?.toast("Please, Write Mobile Number")
            return false
        }
        if (!Validator.isValidMobileNumber(binding?.etEmployeeMobileNo?.text.toString()) || mobileNumber.length < 11) {
            context?.toast("Please, Write Correct Mobile Number")
            return false
        }
        if (leaveReason.trim().isEmpty()) {
            context?.toast("Please, Write Leave Reason")
            return false
        }

        return true
    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Leave Duration")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            fromDate = sdf1.format(it.first)
            toDate = sdf1.format(it.second)
            setDateRangePickerTitle()
        }
    }

    private fun datePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Resumption")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            selectedDate = sdf1.format(it)
            setDatePickerTitle()
        }
    }

    private fun setUpeDepartmentSpinner() {

        val pickupDepartmentList: MutableList<String> = mutableListOf()
        pickupDepartmentList.add("Select Department")
        pickupDepartmentList.add(SessionManager.department)

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupDepartmentList)
        binding?.spinnerDepartmentType?.adapter = spinnerAdapter
        binding?.spinnerDepartmentType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDeptType = position
                selectedDept = pickupDepartmentList[position]
            }
        }
    }

    private fun setUpLeaveTypeSpinner() {

        val pickupLeaveTypeList: MutableList<String> = mutableListOf()
        pickupLeaveTypeList.add("Select Leave Type")
        pickupLeaveTypeList.add("Casual Leave")
        pickupLeaveTypeList.add("Sick Leave")
        pickupLeaveTypeList.add("Earned Leave")
        pickupLeaveTypeList.add("Compensatory Leave")
        pickupLeaveTypeList.add("Other")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupLeaveTypeList)
        binding?.spinnerLeaveType?.adapter = spinnerAdapter
        binding?.spinnerLeaveType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLeaveType = position
                if (position > 0) {
                    binding?.othersLeaveType?.setText(pickupLeaveTypeList[position])
                    if (position == pickupLeaveTypeList.lastIndex) {
                        binding?.othersLeaveType?.text?.clear()
                        binding?.othersLeaveType?.visibility = View.VISIBLE
                    } else {
                        binding?.othersLeaveType?.visibility = View.GONE
                    }
                } else {
                    binding?.othersLeaveType?.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDateRangePickerTitle(){
        binding?.dateRangePicker?.text = "$fromDate - $toDate"
    }

    private fun setDatePickerTitle(){
        binding?.datePicker?.text = selectedDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}