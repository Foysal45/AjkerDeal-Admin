package com.ajkerdeal.app.ajkerdealadmin.ui.retention_report.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsReqBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentRetentionReportDetailsBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.retention_report.RetentionReportViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class RetentionReportDetailsBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentRetentionReportDetailsBottomSheetBinding? = null
    private  var dataBottomSheetAdapter: RetentionReportDetailsBottomSheetAdapter = RetentionReportDetailsBottomSheetAdapter()

    private val viewModel: RetentionReportViewModel by inject()

    private var flagApiBottomSheet: Int = 0
    private var joinDate: String = ""
    private var userId: Int = 0
    private var back: Int = 0
    private var advance: Int = 0
    private var toolbarTitle: String = ""

    companion object {

        fun newInstance(flagApiBottomSheet: Int, joinDate: String, userId: Int, back: Int, advance: Int, toolbarTitle: String): RetentionReportDetailsBottomSheet = RetentionReportDetailsBottomSheet().apply {
            this.flagApiBottomSheet = flagApiBottomSheet
            this.joinDate = joinDate
            this.userId = userId
            this.back = back
            this.advance = advance
            this.toolbarTitle = toolbarTitle
        }
        val tag: String = RetentionReportDetailsBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentRetentionReportDetailsBottomSheetBinding.inflate(inflater, container, false).also {
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
                adapter = dataBottomSheetAdapter
            }
        }
        binding?.RetentionTitleTv?.text = toolbarTitle
    }

    private fun initClickLister(){

    }

    private fun initData(){
        if (flagApiBottomSheet == 1 || flagApiBottomSheet == 2 || flagApiBottomSheet ==3){
            getRetentionMerchantFollowUpReportDetails()
        } else if (flagApiBottomSheet == 4 || flagApiBottomSheet == 5){
            getRetentionMerchantFollowUpReportDetailsBackAdvance()
        }

    }

    private fun getRetentionMerchantFollowUpReportDetails(){
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.getRetentionMerchantFollowUpReportDetails(
            RetentionReportDetailsReqBody(flagApiBottomSheet, joinDate, userId, 0, 0)
        ).observe(viewLifecycleOwner, { model->

            binding?.progressBar?.visibility = View.GONE
            dataBottomSheetAdapter.initLoad(model, flagApiBottomSheet)

        })
    }
    private fun getRetentionMerchantFollowUpReportDetailsBackAdvance(){
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.getRetentionMerchantFollowUpReportDetails(
            RetentionReportDetailsReqBody(flagApiBottomSheet, joinDate, userId, back, advance)
        ).observe(viewLifecycleOwner, { model->

            binding?.progressBar?.visibility = View.GONE
            dataBottomSheetAdapter.initLoad(model, flagApiBottomSheet)

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