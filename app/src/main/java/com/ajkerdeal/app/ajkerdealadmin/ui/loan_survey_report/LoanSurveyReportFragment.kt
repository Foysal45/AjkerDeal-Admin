package com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetLoanSurveyRequestBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetloanSurveyResponseBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLoanSurveyReportBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.bottomsheets.CompanyNameBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.bottomsheets.MonthlyAverageTransactionBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class LoanSurveyReportFragment : Fragment() {

    private var binding: FragmentLoanSurveyReportBinding? = null
    private val viewModel: LoanSurveyReportViewModel by inject()
    private var dataAdapter: LoanSurveyReportAdapter = LoanSurveyReportAdapter()
    var selectedLowerAmount = ""
    var selectedUpperAmount = ""
    var tradeLisenceFlag = ""
    private var isUpperSeelcted: Boolean = false
    private var isLowerSelected: Boolean = false

    private var hasTradeLicenseGlobal: Int = 0

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoanSurveyReportBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initClickListener()
    }

    private fun initClickListener() {
        dataAdapter.onCompanyNameClicked = { model ->
            val tag = CompanyNameBottomSheet.tag
            val dialog = CompanyNameBottomSheet.newInstance(model)
            dialog.show(childFragmentManager, tag)
        }
        dataAdapter.onAverageTransactionClicked = { model ->
            val tag = MonthlyAverageTransactionBottomSheet.tag
            val dialog = MonthlyAverageTransactionBottomSheet.newInstance(model)
            dialog.show(childFragmentManager, tag)
        }
        dataAdapter.onRootClicked = { model ->
            val shouldBePassed = bundleOf("modelOfData" to model)
            findNavController().navigate(
                R.id.action_loanSurveyReportFragment_to_loanSurveyReportDetailsFragment,
                shouldBePassed
            )
        }
        setUpInterestedAmountSpinner(true)
        setUpInterestedAmountSpinner(false)
        setUpTradeLisenceSpinner()
        binding?.datePicker?.setOnClickListener {
            datePicker()
        }
    }

    private fun initView() {
        binding?.recyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
        if (viewModel.startDate == "") {
            val calender = Calendar.getInstance()
            viewModel.startDate = sdf1.format(calender.time)
            viewModel.endDate = sdf1.format(calender.time)
            setDatePickerTitle(true, viewModel.startDate, viewModel.endDate)
        } else {
            setDatePickerTitle(false, viewModel.startDate, viewModel.endDate)
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
                is ViewState.EmptyViewState -> {
                    binding?.emptyView?.visibility = View.VISIBLE
                }

            }
        })

    }

    private fun initData() {
        viewModel.getLoanSurvey(
            GetLoanSurveyRequestBody(fromDate = viewModel.startDate, toDate = viewModel.endDate)
        ).observe(viewLifecycleOwner, { dataList ->
            var newList = listOf<GetloanSurveyResponseBody>()

            newList = when (hasTradeLicenseGlobal) {
                1 -> {
                    calaculateShowingListWhereTradeLisenceTrue(isUpperSeelcted, newList, dataList, isLowerSelected)
                }
                2 -> {
                    calaculateShowingListWhereTradeLisenceFalse(isUpperSeelcted, newList, dataList, isLowerSelected)
                }
                else -> {
                    calaculateShowingListWhereTradeLisenceNoneSelected(isUpperSeelcted, newList, dataList, isLowerSelected)
                }
            }
            dataAdapter.initLoad(newList)
            Timber.d("The data ${newList.size}")
        })
    }

    private fun calaculateShowingListWhereTradeLisenceTrue(
        onlyHigherSelected: Boolean,
        newList: List<GetloanSurveyResponseBody>,
        dataList: List<GetloanSurveyResponseBody>,
        onlyLowerSelected: Boolean,
    ): List<GetloanSurveyResponseBody> {
        var newList1 = newList
        if (!onlyHigherSelected && !onlyHigherSelected) {
            newList1 = dataList.filter { it.hasTradeLicense }
        } else if (onlyHigherSelected && !onlyLowerSelected) {
            newList1 = dataList.filter { it.hasTradeLicense && (it.interestedAmount <= selectedUpperAmount.toInt()) }
        } else if (!onlyHigherSelected && onlyLowerSelected) {
            newList1 = dataList.filter { it.hasTradeLicense && (it.interestedAmount >= selectedLowerAmount.toInt()) }
        } else if (onlyHigherSelected && onlyLowerSelected) {
            newList1 = dataList.filter { it.hasTradeLicense && ((it.interestedAmount <= selectedUpperAmount.toInt()) && (it.interestedAmount >= selectedLowerAmount.toInt())) }
        }
        return newList1
    }

    private fun calaculateShowingListWhereTradeLisenceFalse(
        onlyHigherSelected: Boolean,
        newList: List<GetloanSurveyResponseBody>,
        dataList: List<GetloanSurveyResponseBody>,
        onlyLowerSelected: Boolean,
    ): List<GetloanSurveyResponseBody> {
        var newList1 = newList
        if (!onlyHigherSelected && !onlyHigherSelected) {
            newList1 = dataList.filter { !it.hasTradeLicense }
        } else if (onlyHigherSelected && !onlyLowerSelected) {
            newList1 = dataList.filter { !it.hasTradeLicense && (it.interestedAmount <= selectedUpperAmount.toInt()) }
        } else if (!onlyHigherSelected && onlyLowerSelected) {
            newList1 = dataList.filter { !it.hasTradeLicense && (it.interestedAmount >= selectedLowerAmount.toInt()) }
        } else if (onlyHigherSelected && onlyLowerSelected) {
            newList1 = dataList.filter { !it.hasTradeLicense && ((it.interestedAmount <= selectedUpperAmount.toInt()) && (it.interestedAmount >= selectedLowerAmount.toInt())) }
        }
        return newList1
    }


    private fun calaculateShowingListWhereTradeLisenceNoneSelected(
        onlyHigherSelected: Boolean,
        newList: List<GetloanSurveyResponseBody>,
        dataList: List<GetloanSurveyResponseBody>,
        onlyLowerSelected: Boolean,
    ): List<GetloanSurveyResponseBody> {
        var newList1 = newList
        if (!onlyHigherSelected && !onlyLowerSelected) {
            newList1 = dataList
        } else if (onlyHigherSelected && !onlyLowerSelected) {
            newList1 = dataList.filter { it.interestedAmount <= selectedUpperAmount.toInt() }
        } else if (!onlyHigherSelected && onlyLowerSelected) {
            newList1 = dataList.filter { it.interestedAmount >= selectedLowerAmount.toInt() }
        } else if (onlyHigherSelected && onlyLowerSelected) {
            newList1 = dataList.filter { (it.interestedAmount <= selectedUpperAmount.toInt()) && (it.interestedAmount >= selectedLowerAmount.toInt()) }
        }
        return newList1
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setUpInterestedAmountSpinner(isLower: Boolean) {
        val dataListInterestedAmount: MutableList<String> = mutableListOf(
            "50000", "75000", "100000", "150000", "200000", "250000", "300000",
            "400000", "500000", "600000", "700000", "800000", "900000", "1000000"
        )

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, dataListInterestedAmount)
        if (isLower) {
            dataListInterestedAmount.add(0, "Lower Amount")
            binding?.interestedAmountLowerSpinner?.adapter = spinnerAdapter

            binding?.interestedAmountLowerSpinner?.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedLowerAmount = if (position > 0) {
                            isLowerSelected = true
                            spinnerAdapter.getItem(position)!!
                        } else {
                            isLowerSelected = false
                            ""
                        }
                        if (isLowerSelected) {
                            initData()
                        }
                    }
                }
        } else {
            dataListInterestedAmount.add(0, "Higher Amount")
            binding?.interestedAmountHigherSpinner?.adapter = spinnerAdapter
            binding?.interestedAmountHigherSpinner?.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedUpperAmount = if (position > 0) {
                            isUpperSeelcted = true
                            spinnerAdapter.getItem(position)!!
                        } else {
                            isUpperSeelcted = false
                            ""
                        }
                        if (isUpperSeelcted) {
                            initData()
                        }
                    }
                }
        }
    }

    private fun setUpTradeLisenceSpinner() {
        val tradeLisence: MutableList<String> = mutableListOf("Trade Licence", "Yes", "No", "Both")
        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, tradeLisence)
        binding?.tradeLisenseSpinner?.adapter = spinnerAdapter
        binding?.tradeLisenseSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    tradeLisenceFlag = if (position > 0) {
                        when (position) {
                            1 -> {
                                initData()
                                hasTradeLicenseGlobal = 1
                            }
                            2 -> {
                                initData()
                                hasTradeLicenseGlobal = 2
                            }
                            3 -> {
                                initData()
                                hasTradeLicenseGlobal = 3
                            }
                        }
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun datePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select Date range")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            viewModel.startDate = sdf.format(it.first)
            viewModel.endDate = sdf.format(it.second)
            initData()
            setDatePickerTitle(false, viewModel.startDate, viewModel.endDate)
        }
    }

    private fun setDatePickerTitle(initLoad: Boolean = false, fD: String = "", tD: String = "") {
        if (initLoad) {
            binding?.datePicker?.text =
                "${DigitConverter.formatDate(viewModel.startDate, "yyyy-MM-dd", "dd MMM")} " +
                        "- ${DigitConverter.formatDate(viewModel.endDate, "yyyy-MM-dd", "dd MMM")}"
        } else {
            binding?.datePicker?.text =
                "${DigitConverter.formatDate(fD, "yyyy-MM-dd", "dd MMM")} " +
                        "- ${DigitConverter.formatDate(tD, "yyyy-MM-dd", "dd MMM")}"
        }
    }
}