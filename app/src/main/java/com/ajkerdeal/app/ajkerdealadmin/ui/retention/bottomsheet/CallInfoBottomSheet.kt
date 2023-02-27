package com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.call_info.CalledInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentCallInfoBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.RetentionMerchantListViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class CallInfoBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentCallInfoBottomSheetBinding? = null

    private val viewModel: RetentionMerchantListViewModel by inject()

    private lateinit var model: RetentionMerchentListModel
    private var selectedType = 0
    private var mobileNumber = ""
    private var callDuration = 0
    private var callSummary = ""
    private lateinit var pickupNumberList: MutableList<String>

    companion object {
        fun newInstance(model: RetentionMerchentListModel): CallInfoBottomSheet = CallInfoBottomSheet().apply {
            this.model = model
        }
        val tag: String = CallInfoBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        val dialog: BottomSheetDialog? = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(false)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parent?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            with(BottomSheetBehavior.from(bottomSheet)) {
                skipCollapsed = true
                isHideable = false
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentCallInfoBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()

        binding?.titleTV?.text = "Call Information (${model.companyName})"

        binding?.saveBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val requestBody = CalledInfoRequest(
                    model.courierUserId, SessionManager.dtUserId,
                    mobileNumber, callDuration,
                    callSummary
                )

                context?.toast("$requestBody")
                viewModel.updateCalledMerchant(requestBody).observe(viewLifecycleOwner, Observer { model ->
                    if (model.merchantCalledId > 0) {
                        dismiss()
                        context?.toast("Updated successfully")
                    } else {
                        context?.toast("Failed! Try again.")
                    }
                })

            }
        }
    }

    private fun validate(): Boolean {

        if (binding?.callDuration?.text.toString().isNotEmpty()) {
            callDuration = binding?.callDuration?.text.toString().toInt()
        }
        callSummary = binding?.callSummary?.text.toString()

        /*if (selectedType == 0) {
            context?.toast("Please, Select Mobile Number")
            return false
        }*/

        if (selectedType == pickupNumberList.size-1) {
            mobileNumber = binding?.otherMobileNumber?.text.toString()
        }

        if (SessionManager.dtUserId == 0) {
            context?.toast("User id not found!, try login again!")
            return false
        }

        if (mobileNumber.isEmpty()) {
            context?.toast("Please, Enter Mobile Number")
            return false
        }

        if (mobileNumber.length != 11) {
            context?.toast("Please, Enter Valid Mobile Number")
            return false
        }

        if (callDuration == 0) {
            context?.toast("Please, Write Call Duration")
            return false
        }

        if (callSummary.trim().isEmpty()) {
            context?.toast("Please, Write Call Summary")
            return false
        }

        return true
    }

    private fun setUpSpinner() {

        pickupNumberList = mutableListOf()
        pickupNumberList.add(model.mobile ?: "")
        if (!model.alterMobile.isNullOrEmpty()){
            pickupNumberList.add(model.alterMobile ?: "")
        }
        if (!model.bkashNumber.isNullOrEmpty()){
            pickupNumberList.add(model.bkashNumber ?: "")
        }
        pickupNumberList.add("Other")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupNumberList)
        binding?.spinnerCalledNumber?.adapter = spinnerAdapter
        binding?.spinnerCalledNumber?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedType = position
                binding?.otherMobileNumber?.setText(pickupNumberList[position])
                if (position == pickupNumberList.lastIndex) {
                    mobileNumber = ""
                    binding?.otherMobileNumber?.text?.clear()
                    binding?.otherMobileNumber?.visibility = View.VISIBLE
                    binding?.otherMobileNumber?.requestFocus()
                } else {
                    mobileNumber = pickupNumberList[position]
                    binding?.otherMobileNumber?.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}