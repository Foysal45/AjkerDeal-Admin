package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainCommentUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainHistoryBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class ComplainHistoryBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentComplainHistoryBottomSheetBinding? = null
    private val viewModel: ComplainHistoryViewModel by inject()

    private var dataAdapter: ComplainHistoryAdapter = ComplainHistoryAdapter()

    private var selectedBookingCode = 0
    private var complainDate: String = ""

    companion object {

        fun newInstance(bookingCode: Int, complainDate: String): ComplainHistoryBottomSheet = ComplainHistoryBottomSheet().apply {
            this.selectedBookingCode = bookingCode
            this.complainDate = complainDate
        }

        val tag: String = ComplainHistoryBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentComplainHistoryBottomSheetBinding.inflate(inflater, container, false).also {
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
                adapter = dataAdapter
            }
        }
        dataAdapter.complainDate = complainDate
    }

    private fun initClickLister(){

        binding?.submitBtn?.setOnClickListener{
            hideKeyboard()
            val comments = binding?.complainCommentsTV?.text.toString()
            if (comments.isEmpty()){
                context?.toast("Write your comment")
            }else{
                updateCommentsForAdminApp(comments)
            }
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

    private fun updateCommentsForAdminApp(comments: String){
        val requestBody = ComplainCommentUpdateRequest(selectedBookingCode.toString(), comments, SessionManager.dtUserId)
        viewModel.updateCommentsForAdminApp(requestBody).observe(viewLifecycleOwner, Observer { response->
            if (response > 0){
                context?.toast("আপনার অভিযোগ / মতামত সাবমিট হয়েছে")
                dialog?.dismiss()
            }else{
                context?.toast("কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initData(){
        viewModel.getComplainHistory(selectedBookingCode).observe(viewLifecycleOwner, Observer { list->
            if (list.isNullOrEmpty()){
                binding?.emptyView?.visibility = View.VISIBLE
            }else{
                binding?.emptyView?.visibility = View.GONE
                dataAdapter.initLoad(list)
                val model = list.first()
                binding?.orderId?.text = "OrderId: DT-${model.bookingCode}"
                binding?.merchantName?.text = "Merchant: ${model.merchantName}"
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