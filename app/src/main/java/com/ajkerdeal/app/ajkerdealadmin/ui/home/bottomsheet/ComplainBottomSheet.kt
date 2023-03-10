package com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainAddBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.ComplainViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import androidx.lifecycle.Observer
import java.util.*
import kotlin.concurrent.thread

class ComplainBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentComplainAddBottomSheetBinding? = null
    private val viewModel: ComplainViewModel by inject()

    private var selectedType = 0

    private var orderId: String? = ""

    companion object {
        fun newInstance(orderId: String?): ComplainBottomSheet = ComplainBottomSheet().apply {
            this.orderId = orderId
        }
        val tag: String = ComplainBottomSheet::class.java.name
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
        return FragmentComplainAddBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()

        binding?.orderCodeTV?.setText(orderId?.uppercase())

        binding?.submitBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {

                val orderCode = binding?.orderCodeTV?.text.toString().trim()
                val complain = binding?.complainTV?.text.toString().trim()
                val code = orderCode.toUpperCase(Locale.US).replace("DT-", "")

                val requestBody = ComplainRequest(orderCode, complain, SessionManager.dtUserId.toString())
                viewModel.submitComplain(requestBody).observe(viewLifecycleOwner, Observer { complainStatus->
                    when {
                        complainStatus > 0 -> {
                            binding?.orderCodeTV?.text?.clear()
                            binding?.complainTV?.text?.clear()
                            binding?.spinnerComplainType?.setSelection(0)

                            context?.toast("??????????????? ?????????????????? / ??????????????? ?????????????????? ???????????????")
                            dismiss()
                        }
                        complainStatus == -1 -> {
                            context?.toast("?????? ???????????????????????? ???????????????????????? ????????? ???????????????")
                        }
                        else -> {
                            context?.toast("??????????????? ???????????? ?????????????????? ???????????????, ???????????? ?????????????????? ????????????")
                        }
                    }
                })
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.ShowMessage -> {
                    context?.toast(state.message)
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

        val orderCode = binding?.orderCodeTV?.text.toString()
        val complain = binding?.complainTV?.text.toString()

        if (selectedType == 1 || selectedType == 2) {
            if (orderCode.trim().isEmpty()) {
                context?.toast("?????????????????? ???????????? ???????????????")
                return false
            }
        }

        if (orderCode.trim().isNotEmpty() && !isValidDTCode(orderCode)) {
            context?.toast("???????????? ?????????????????? ???????????? ???????????????")
            return false
        }

        if (selectedType == 0) {
            context?.toast("???????????????????????? ???????????? ????????????????????? ????????????")
            return false
        }

        if (selectedType == 12) {
            if (complain.trim().isEmpty()) {
                context?.toast("??????????????? ?????????????????? / ??????????????? ???????????????")
                return false
            }
        }

        return true
    }

    private fun setUpSpinner() {

        val pickupDistrictList: MutableList<String> = mutableListOf()
        pickupDistrictList.add("???????????????????????? ???????????? ????????????????????? ????????????")
        pickupDistrictList.add("???????????????????????? ???????????? ????????????????????? ???????????????????????? ???????????? ?????????")
        pickupDistrictList.add("????????????????????? ????????????????????? ???????????? ???????????? ????????? ?????????")
        pickupDistrictList.add("COD ????????????????????? ???????????? ????????? ?????????")
        pickupDistrictList.add("????????????????????? ???????????? ????????????????????? ????????? ?????????")
        pickupDistrictList.add("????????? ???????????????????????? ????????????????????? ????????? ?????????????????????")
        pickupDistrictList.add("????????????????????? ??????????????????")
        pickupDistrictList.add("?????????????????? ????????????")
        pickupDistrictList.add("??????????????????????????? ????????????????????? ?????????????????????")
        pickupDistrictList.add("????????????????????? ???????????? ????????????????????????")
        pickupDistrictList.add("???????????????????????? ??????????????? ?????????????????? ???????????? ??????????????? ????????????")
        pickupDistrictList.add("?????? ?????????????????????????????? ??????????????? ?????????????????? ???????????? ???????????????")
        pickupDistrictList.add("???????????? ????????????????????????")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupDistrictList)
        binding?.spinnerComplainType?.adapter = spinnerAdapter
        binding?.spinnerComplainType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedType = position
                binding?.orderCodeTV?.visibility = View.GONE
                if (position > 0) {
                    binding?.complainTV?.setText(pickupDistrictList[position])
                    binding?.orderCodeTV?.visibility = View.VISIBLE
                    if (position == pickupDistrictList.lastIndex) {
                        binding?.complainTV?.text?.clear()
                        binding?.complainTV?.visibility = View.VISIBLE
                    }else{
                        binding?.complainTV?.visibility = View.GONE
                    }
                } else {
                    binding?.complainTV?.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}