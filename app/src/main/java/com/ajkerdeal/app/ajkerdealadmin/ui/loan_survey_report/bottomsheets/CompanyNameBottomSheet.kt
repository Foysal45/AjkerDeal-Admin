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
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentCompanyNameLoanSurveyBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMonthlyAverageTransactionBottomsheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.LoanSurveyReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet.CallInfoBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class CompanyNameBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentCompanyNameLoanSurveyBottomSheetBinding? = null
    private val viewModel: LoanSurveyReportViewModel by inject()

    private var dataAdapter: MonthlyAverageTransactionAdapter = MonthlyAverageTransactionAdapter()

    private var courrierUserId = 0
    private var complainDate: String = ""

    companion object {
        fun newInstance(model: GetloanSurveyResponseBody): CompanyNameBottomSheet = CompanyNameBottomSheet().apply {
            this.courrierUserId = model.courierUserId
        }

        val tag: String = CompanyNameBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentCompanyNameLoanSurveyBottomSheetBinding.inflate(inflater, container, false).also {
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

    }

    private fun initClickLister() {

    }


    private fun initData() {
        if (courrierUserId != 0)
            viewModel.getCourierUserInfo(courrierUserId).observe(viewLifecycleOwner, { dataModel ->
                var count = 0
                if (dataModel.userName != "") {
                    binding?.personNameLayout?.visibility = View.VISIBLE
                    binding?.personName?.text = dataModel.userName
                }
                if (dataModel.mobile != "") {
                    binding?.contractMobileLayout?.visibility = View.VISIBLE
                    binding?.mobileText?.visibility = View.VISIBLE
                    binding?.mobileText?.text = dataModel.mobile
                }
                if (dataModel.alterMobile != "") {
                    binding?.alterMobileLayout?.visibility = View.VISIBLE
                    binding?.mobileText?.text = dataModel.alterMobile
                }
                if (dataModel.address != "") {
                    binding?.addressLayout?.visibility = View.VISIBLE
                    binding?.addressText?.text = dataModel.address
                }
                if (dataModel.emailAddress != "") {
                    binding?.emailLayout?.visibility = View.VISIBLE
                    binding?.emailAddressText?.text = dataModel.emailAddress
                }
                if (dataModel.joinDate != "") {
                    binding?.joinDateLayout?.visibility = View.VISIBLE
                    binding?.joinDateText?.text = DigitConverter.formatDate(dataModel.joinDate, "yyyy-MM-dd", "dd MMM, YYYY")
                }
                if (dataModel.companyName != "") {
                    binding?.CompanyNameLayout?.visibility = View.VISIBLE
                    binding?.CompanyNameText?.text = dataModel.companyName
                }
                if (dataModel.fburl != "") {
                    binding?.facebookUrlLayout?.visibility = View.VISIBLE
                    binding?.FbUrlText?.text = dataModel.fburl
                }
                if (dataModel.webURL != "") {
                    binding?.webUrlLayout?.visibility = View.VISIBLE
                    binding?.WebUrlText?.text = dataModel.webURL
                }
                if (dataModel.categoryName != "") {
                    binding?.categoryLayout?.visibility = View.VISIBLE
                    binding?.CategoryText?.text = dataModel.categoryName
                }
                if (dataModel.accountName != "") {
                    binding?.bankAccountNameLayout?.visibility = View.VISIBLE
                    binding?.BancAccountNameText?.text = dataModel.accountName
                }
                if (dataModel.accountNumber != "") {
                    binding?.bankAccountNumberLayout?.visibility = View.VISIBLE
                    binding?.BankAccountNumberText?.text = dataModel.accountNumber
                }
                if (dataModel.accountNumber != "") {
                    binding?.bankAccountNumberLayout?.visibility = View.VISIBLE
                    binding?.BankAccountNumberText?.text = dataModel.accountNumber
                }
                if (dataModel.bankName != "") {
                    binding?.bankNameLayout?.visibility = View.VISIBLE
                    binding?.BankText?.text = dataModel.bankName
                }
                if (dataModel.branchName != "") {
                    binding?.branchNameLayout?.visibility = View.VISIBLE
                    binding?.branchNameText?.text = dataModel.branchName
                }
                if (dataModel.gender != "") {
                    binding?.genderLayout?.visibility = View.VISIBLE
                    binding?.genderText?.text = dataModel.gender
                }
                if (dataModel.merchantReview != 0) {
                    binding?.merchantReviewLayout?.visibility = View.VISIBLE
                    binding?.merchantReviewText?.text = dataModel.merchantReview.toString()
                }
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