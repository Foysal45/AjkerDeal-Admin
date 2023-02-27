package com.ajkerdeal.app.ajkerdealadmin.ui.retention_report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportReqBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentRetentionReportBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.retention_report.bottomSheet.RetentionReportDetailsBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class RetentionReportFragment : Fragment() {

    private var binding: FragmentRetentionReportBinding? = null
    private val viewModel: RetentionReportViewModel by inject()
    private lateinit var retentionReportAdapter: RetentionReportAdapter

    private var retentionDate = "2001-01-01"
    private var displayRetentionDate = "2001-01-01"

    private var back: Int = 2
    private var advance: Int = 2

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM", Locale.US)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRetentionReportBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initData()
        initClickLister()

    }

    private fun init(){

        val calendar = Calendar.getInstance()
        retentionDate = sdf.format(calendar.time)
        displayRetentionDate = sdf1.format(calendar.time)
        setRetentionDatePickerTitle()

        //back advance days spinner setup
        binding?.let { setUpSpinner(it.spinnerBackDaysSelection, 1) }
        binding?.let { setUpSpinner(it.spinnerAdvanceDaysSelection, 2) }

        retentionReportAdapter = RetentionReportAdapter()
        with(binding?.retentionReportRV!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = retentionReportAdapter
        }

    }

    private fun initClickLister(){
        retentionReportAdapter.onItemClicked = { model, flagApi ->
            when (flagApi) {
                1 -> {
                    if (model.merchantCalled  > 0){
                        goToRetentionReportDetailsBottomSheet(1, retentionDate, model.retentionUserId, "Called Merchant")
                    } else{
                        context?.toast("No Data Found")
                    }
                }
                2 -> {
                    if (model.merchantVisited  > 0) {
                        goToRetentionReportDetailsBottomSheet(2, retentionDate, model.retentionUserId, "Visited Merchant")
                    } else{
                        context?.toast("No Data Found")
                    }
                }
                3 -> {
                    if (model.orderData  > 0){
                        goToRetentionReportDetailsBottomSheet(3, retentionDate, model.retentionUserId, "Regular Merchant")
                    } else{
                        context?.toast("No Data Found")
                    }
                }
                4-> {
                    if (model.orderDatanot > 0){
                        goToRetentionReportDetailsBottomSheet(4, retentionDate, model.retentionUserId, "Today Order")
                    } else{
                        context?.toast("No Data Found")
                    }
                }
                5-> {
                    if (model.orderDataFuture > 0){
                        goToRetentionReportDetailsBottomSheet(5, retentionDate, model.retentionUserId, "Advance Order")
                    } else{
                        context?.toast("No Data Found")
                    }
                }
            }
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            fetchRetentionData(retentionDate,back,advance)
        }

        binding?.retentionReportDatePicker?.setOnClickListener {
            retentionReportDatePicker()
        }
    }

    private fun goToRetentionReportDetailsBottomSheet(flag: Int, date: String, id: Int, title: String){
        val tag = RetentionReportDetailsBottomSheet.tag
        val dialog = RetentionReportDetailsBottomSheet.newInstance(flag, date, id, back, advance, title)
        dialog.show(childFragmentManager, tag)
    }

    private fun fetchRetentionData(joinDate: String, back: Int, advance: Int) {

        val requestBody = RetentionReportReqBody(joinDate, back, advance)
        binding?.swipeRefresh?.isRefreshing = true
        viewModel.getRetentionMerchantFollowUpDetails(requestBody).observe(viewLifecycleOwner, { model ->
            binding?.swipeRefresh?.isRefreshing = false
            retentionReportAdapter.initLoad(model, back, advance)
        })
    }

    private fun retentionReportDatePicker(){
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select Date")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            retentionDate = sdf.format(it)
            displayRetentionDate = sdf1.format(it)
            setRetentionDatePickerTitle()
            fetchRetentionData(retentionDate, back, advance)
        }
    }

    private fun setRetentionDatePickerTitle(){
        val msg = displayRetentionDate
        binding?.retentionReportDatePicker?.text = msg
    }

    private fun initData(){
        fetchRetentionData(retentionDate, back, advance)
    }

    private fun setUpSpinner(spinner: AppCompatSpinner, spinnerFlag: Int) {

        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        if (spinnerFlag == 1){
            pickupBreakReasonList.add("Back (2D)")
            pickupBreakReasonList.add("Back (3D)")
            pickupBreakReasonList.add("Back (4D)")
            pickupBreakReasonList.add("Back (5D)")
            pickupBreakReasonList.add("Back (6D)")
            pickupBreakReasonList.add("Back (7D)")
        } else if(spinnerFlag == 2){
            pickupBreakReasonList.add("Advance (2D)")
            pickupBreakReasonList.add("Advance (3D)")
            pickupBreakReasonList.add("Advance (4D)")
            pickupBreakReasonList.add("Advance (5D)")
            pickupBreakReasonList.add("Advance (6D)")
            pickupBreakReasonList.add("Advance (7D)")
        }

        var fetch = false
        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item_retention, pickupBreakReasonList)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(spinnerFlag){
                    1->{
                        when(position){
                            0->{
                                back = 2
                            }
                            1->{
                                back = 3
                                fetch = true
                            }
                            2->{
                                back = 4
                                fetch = true
                            }
                            3->{
                                back = 5
                                fetch = true
                            }
                            4->{
                                back = 6
                                fetch = true
                            }
                            5->{
                                back = 7
                                fetch = true
                            }
                        }
                    } //Back Days
                    2->{
                        when(position){
                            0->{
                                advance = 2
                            }
                            1->{
                                advance = 3
                                fetch = true
                            }
                            2->{
                                advance = 4
                                fetch = true
                            }
                            3->{
                                advance = 5
                                fetch = true
                            }
                            4->{
                                advance = 6
                                fetch = true
                            }
                            5->{
                                advance = 7
                                fetch = true
                            }
                        }
                    } //Advance Days
                }
                if(fetch){
                    fetchRetentionData(retentionDate, back, advance)
                }
            }
        }
    }

}