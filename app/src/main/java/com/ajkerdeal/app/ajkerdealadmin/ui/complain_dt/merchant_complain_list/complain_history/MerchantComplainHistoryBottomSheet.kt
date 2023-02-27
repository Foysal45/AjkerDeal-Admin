package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list.complain_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R

import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMerchantComplainHistoryBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class MerchantComplainHistoryBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentMerchantComplainHistoryBottomSheetBinding? = null
    private var dataAdapterMerchant: MerchantComplainHistoryAdapter = MerchantComplainHistoryAdapter()
    private val viewModelMerchant: MerchantComplainHistoryViewModel by inject()

    private var selectedBookingCode = 0

    companion object {

        fun newInstance(bookingCode: Int): MerchantComplainHistoryBottomSheet = MerchantComplainHistoryBottomSheet().apply {
            selectedBookingCode = bookingCode
        }

        val tag: String = MerchantComplainHistoryBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentMerchantComplainHistoryBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initClickLister()
        initData()
    }

    private fun initView() {
        binding?.recyclerview?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapterMerchant
            }
        }
    }

    private fun initClickLister(){
        viewModelMerchant.viewState.observe(viewLifecycleOwner, Observer { state ->
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

    private fun initData(){
        viewModelMerchant.getComplainHistory(selectedBookingCode).observe(viewLifecycleOwner, Observer { model->
            if (model.isNullOrEmpty()){
                binding?.emptyView?.visibility = View.VISIBLE
            }else{
                binding?.emptyView?.visibility = View.GONE
                dataAdapterMerchant.initLoad(model)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(true)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parentLayout?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = false

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}