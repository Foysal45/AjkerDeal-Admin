package com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.bottomsheets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetloanSurveyResponseBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMonthlyAverageTransactionBottomsheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.LoanSurveyReportViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class MonthlyAverageTransactionBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentMonthlyAverageTransactionBottomsheetBinding? = null
    private val viewModel: LoanSurveyReportViewModel by inject()

    private var dataAdapter: MonthlyAverageTransactionAdapter = MonthlyAverageTransactionAdapter()

    private var courrierUserId = 0
    private var complainDate: String = ""

    companion object {
        fun newInstance(model: GetloanSurveyResponseBody): MonthlyAverageTransactionBottomSheet = MonthlyAverageTransactionBottomSheet().apply {
            this.courrierUserId = model.courierUserId
        }
        val tag: String = CompanyNameBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentMonthlyAverageTransactionBottomsheetBinding.inflate(inflater, container, false).also {
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
    }

    private fun initClickLister() {

    }

    private fun initData() {
        viewModel.MonthWiseTotalCollectionAmount(this.courrierUserId).observe(viewLifecycleOwner, {
            dataAdapter.initLoad(it)
            thread {
                activity?.runOnUiThread {
                    val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    val dynamicHeight = binding?.parentLayout?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet!!).peekHeight = dynamicHeight
                }
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