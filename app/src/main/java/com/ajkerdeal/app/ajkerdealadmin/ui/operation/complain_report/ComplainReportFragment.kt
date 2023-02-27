package com.ajkerdeal.app.ajkerdealadmin.ui.operation.complain_report

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainReportBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.ComplainViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class ComplainReportFragment : Fragment() {

    private var binding: FragmentComplainReportBinding? = null
    private val viewModel: ComplainViewModel by inject()
    private var dataAdapter: ComplainReportAdapter = ComplainReportAdapter()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM", Locale.US)

    private var fromDateDisplay = ""
    private var toDateDisplay = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentComplainReportBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickListener()
        initData()
        fetchAssignedComplainCount()

    }

    private fun initClickListener() {

        dataAdapter.onItemClick = { _, _ ->

        }

        dataAdapter.onTotalClick = { dataList ->
            if (dataList.totalComplain > 0) {
                findNavController().navigate(
                    R.id.action_complainReportFragment_to_complainDetailsFragment,
                    complainType(
                        typeFlag = 1,
                        updatedBy = 0,
                        isIssueSolved = 1,
                        assignedTo = dataList.assignedTo,
                        fullName = dataList.fullName,
                    )
                )
            } else {
                context?.toast("No data found")
            }
        }

        dataAdapter.onPendingClick = { dataList ->
            if (dataList.pendingComplain > 0) {
                findNavController().navigate(
                    R.id.action_complainReportFragment_to_complainDetailsFragment,
                    complainType(
                        typeFlag = 1,
                        updatedBy = 0,
                        isIssueSolved = 0,
                        assignedTo = dataList.assignedTo,
                        fullName = dataList.fullName
                    )
                )
            } else {
                context?.toast("No data found")
            }
        }

        dataAdapter.onSolvedClick = { dataList ->
            if (dataList.solvedComplain > 0) {
                findNavController().navigate(
                    R.id.action_complainReportFragment_to_complainDetailsFragment,
                    complainType(
                        typeFlag = 1,
                        updatedBy = 0,
                        isIssueSolved = 9,
                        assignedTo = dataList.assignedTo,
                        fullName = dataList.fullName
                    )
                )
            } else {
                context?.toast("No data found")
            }
        }

        dataAdapter.onTouchedClick = { dataList ->
            if (dataList.touchedComplain > 0) {
                findNavController().navigate(
                    R.id.action_complainReportFragment_to_complainDetailsFragment,
                    complainType(
                        typeFlag = 2,
                        updatedBy = 1,
                        isIssueSolved = 0,
                        assignedTo = dataList.assignedTo,
                        fullName = dataList.fullName
                    )
                )
            } else {
                context?.toast("No data found")
            }
        }

        dataAdapter.onUntouchedClick = { dataList ->
            if (dataList.untouchedComplain > 0) {
                findNavController().navigate(
                    R.id.action_complainReportFragment_to_complainDetailsFragment,
                    complainType(
                        typeFlag = 2,
                        updatedBy = 0,
                        isIssueSolved = 0,
                        assignedTo = dataList.assignedTo,
                        fullName = dataList.fullName
                    )
                )
            } else {
                context?.toast("No data found")
            }
        }
        binding?.complainReportDatePicker?.setOnClickListener {
            dateRangePicker()
        }
    }

    private fun complainType(typeFlag: Int, updatedBy: Int, isIssueSolved: Int, assignedTo: Int, fullName: String): Bundle {
        return bundleOf(
            "typeFlag" to typeFlag,
            "updatedBy" to updatedBy,
            "isIssueSolved" to isIssueSolved,
            "fromDate" to viewModel.fromDate,
            "toDate" to viewModel.toDate,
            "assignedTo" to assignedTo,
            "fullName" to fullName
        )
    }

    private fun initView() {
        binding?.recyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }

    private fun initData() {
        if (viewModel.fromDate == "") {
            val calendar = Calendar.getInstance()
            viewModel.fromDate = sdf.format(calendar.time)
            viewModel.toDate = sdf.format(calendar.time)
            fromDateDisplay = sdf1.format(calendar.time)
            toDateDisplay = sdf1.format(calendar.time)
            setDateRangePickerTitle(true, viewModel.fromDate, viewModel.toDate)
        } else {
            setDateRangePickerTitle(false, viewModel.fromDate, viewModel.toDate)
        }

    }

    private fun fetchAssignedComplainCount() {
        val requestBody = AutoAssignComplainCountRequest(fromDate = viewModel.fromDate, toDate = viewModel.toDate)
        viewModel.assignedComplainCount(requestBody).observe(viewLifecycleOwner, { model ->
            dataAdapter.initLoad(model)
        })
    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select date range")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            viewModel.fromDate = sdf.format(it.first)
            viewModel.toDate = sdf.format(it.second)
            fromDateDisplay = sdf1.format(it.first)
            toDateDisplay = sdf1.format(it.second)
            setDateRangePickerTitle(false, viewModel.fromDate, viewModel.toDate)
            fetchAssignedComplainCount()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDateRangePickerTitle(initLoad: Boolean = false, fromDate: String = "", toDate: String = "") {
        if (initLoad) {
            binding?.complainReportDatePicker?.text = "$fromDateDisplay - $toDateDisplay"
        } else {
            binding?.complainReportDatePicker?.text = "${DigitConverter.formatDate(fromDate, "yyyy-MM-dd", "dd MMM")} - ${DigitConverter.formatDate(toDate, "yyyy-MM-dd", "dd MMM")}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}