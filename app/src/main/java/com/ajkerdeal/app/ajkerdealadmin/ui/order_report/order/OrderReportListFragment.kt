package com.ajkerdeal.app.ajkerdealadmin.ui.order_report.order

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentOrderReportListBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class OrderReportListFragment : Fragment() {
    private var binding: FragmentOrderReportListBinding? = null
    private  var dataAdapter: OrderReportAdapter = OrderReportAdapter()
    private val viewModel: OrderReportViewModel by inject()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM, yyyy", Locale.US)

    private var selectedDate = "2001-01-01"
    private var fromDate = "2001-01-01"
    private var toDate = "2001-01-01"
    private var selectedDateFormatted = ""

    companion object {
        fun newInstance(): OrderReportListFragment = OrderReportListFragment().apply {}
        val tag: String = OrderReportListFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentOrderReportListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        initClickLister()
        initPreselectedDate()
    }

    private fun initView() {
        binding?.recycleView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }

    private fun initClickLister(){
        binding?.dateRangePicker?.setOnClickListener {
            dateRangePicker()
        }

        dataAdapter.onItemClick = { _, _ ->
            showOrderWiseMerchantList()
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

    private fun initPreselectedDate() {
        val date = Date()
        selectedDate = sdf.format(date)
        selectedDateFormatted = sdf1.format(date)
        setDateRangePickerTitle()
        fetchEmployeeWorkingReport(selectedDate)
    }

    private fun fetchEmployeeWorkingReport(date: String){

        binding?.emptyView?.isVisible = true
        binding?.emptyView?.text = "Under development"
        /*viewModel.fetchEmployeeWorkingReport(date).observe(viewLifecycleOwner, Observer { list->
            dataAdapter.initLoad(list)
            binding?.emptyView?.isVisible = list.isEmpty()
        })*/
    }

    private fun showOrderWiseMerchantList() {
        findNavController().navigate(R.id.nav_merchant_list)
    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select date")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            fromDate = sdf.format(it.first)
            toDate = sdf.format(it.first)
            selectedDate = fromDate
            selectedDateFormatted = sdf1.format(it.first)
            setDateRangePickerTitle()
            fetchEmployeeWorkingReport(selectedDate)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDateRangePickerTitle(){
        binding?.dateRangePicker?.text = "$fromDate - $toDate"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}