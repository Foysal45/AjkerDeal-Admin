package com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesActiveMerchantResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TelesalesRequestBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentTeleSalesActiveMerchantBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class TeleSalesActiveMerchantFragment : Fragment() {

    private var binding: FragmentTeleSalesActiveMerchantBinding? = null
    private var dataAdapter: TelesalesAdapter = TelesalesAdapter()
    private val viewModel: TeleSalesViewModel by inject()

    private var fromDateDisplay = ""
    private var toDateDisplay = ""

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM", Locale.US)

    companion object {
        fun newInstance(): TeleSalesActiveMerchantFragment = TeleSalesActiveMerchantFragment().apply {}
        val tag: String = TeleSalesActiveMerchantFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentTeleSalesActiveMerchantBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initClickLister()
    }

    private fun initView() {

        if(viewModel.fromDate == ""){
            val calender = Calendar.getInstance()
            viewModel.fromDate = sdf.format(calender.time)
            viewModel.toDate = sdf.format(calender.time)
            fromDateDisplay = sdf1.format(calender.time)
            toDateDisplay = sdf1.format(calender.time)
            setDateRangePickerTitle(true, viewModel.fromDate, viewModel.toDate)
        }else{
            setDateRangePickerTitle(false, viewModel.fromDate, viewModel.toDate)
        }

        with(binding?.telesalesRecyclerView!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
        }

        fetchTelesalesData(viewModel.fromDate, viewModel.toDate)

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

    private fun initClickLister() {
        binding?.datePicker?.setOnClickListener {
            datePicker()
        }

        dataAdapter.recentlyInterested = { model ->
            navigate(model, 1, "Recently Interested")
        }
        dataAdapter.notInterested = { model ->
            navigate(model, 2, "Not Interested")
        }
        dataAdapter.busnessClosed = { model ->
            navigate(model, 3, "Business Closed")
        }
        dataAdapter.didntPicked = { model ->
            navigate(model, 4, "Didn't Picked")
        }
        dataAdapter.latePicked = { model ->
            navigate(model, 5, "Late Picked")
        }
    }

    private fun navigate(model: TeleSalesActiveMerchantResponse, status: Int, title: String) {
        findNavController().navigate(
            R.id.action_telesalesMenu_to_teleSalesDetailsFragment, bundleOf(
                "date" to model.year.toString() + "-" + model.month.toString() + "-" + model.day.toString(),
                "status" to status,
                "title" to title
            )
        )
    }


    private fun datePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select Date")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {

            viewModel.fromDate = sdf.format(it.first)
            viewModel.toDate = sdf.format(it.second)
            fromDateDisplay = sdf1.format(it.first)
            toDateDisplay = sdf1.format(it.second)
            fetchTelesalesData(viewModel.fromDate, viewModel.toDate)
            setDateRangePickerTitle(false, viewModel.fromDate, viewModel.toDate)
        }
    }

    /*private fun setDatePickerTitle(initLoad: Boolean = false, fD: String = "", tD: String = "") {
        if (initLoad) {
            binding?.datePicker?.text = "$fD - $tD"
        }else{
            binding?.datePicker?.text = "$fromDateDisplay - $toDateDisplay"
        }
    }*/

    @SuppressLint("SetTextI18n")
    private fun setDateRangePickerTitle(initLoad: Boolean = false, fromDate: String = "", toDate: String = "") {
        if (initLoad) {
            binding?.datePicker?.text = "$fromDateDisplay - $toDateDisplay"
        } else {
            binding?.datePicker?.text = "${DigitConverter.formatDate(fromDate, "yyyy-MM-dd", "dd MMM")} - ${DigitConverter.formatDate(toDate, "yyyy-MM-dd", "dd MMM")}"
        }
    }


    private fun fetchTelesalesData(fromDate: String, toDate: String) {
        val requestBody = TelesalesRequestBody(
            fromDate = fromDate,
            toDate = toDate
        )
        viewModel.getTelesalesActiveMerchantList(requestBody).observe(viewLifecycleOwner, Observer { list ->
            dataAdapter.initLoad(list)
            binding?.emptyView?.isVisible = list.isEmpty()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}