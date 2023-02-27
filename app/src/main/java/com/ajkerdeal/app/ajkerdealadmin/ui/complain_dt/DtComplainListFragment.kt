package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainListRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentDtComplainListBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class DtComplainListFragment : Fragment() {

    private var binding: FragmentDtComplainListBinding? = null
    private val viewModel: ComplainViewModel by inject()

    private var dataAdapter: DtComplainListAdapter = DtComplainListAdapter()

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM", Locale.US)

    private var fromDate = "2001-01-01"
    private var toDate = "2001-01-01"
    private var fromDateDisplay = ""
    private var toDateDisplay = ""
    private var status = ""
    private val adminList = listOf(1, 2, 11, 67, 240, 783)
    private var isFilterByUser: Int = 1

    // 0 for pending complain and 9 for solved complain
    //private var isIssueSolved = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentDtComplainListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData(0)
        initClickLister()
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

    private fun initData(isIssueSolved:Int) {
        //Fetch 3 days data
        val calendar = Calendar.getInstance()
        toDate = sdf.format(calendar.time)
        toDateDisplay = sdf1.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, -3)
        fromDate = sdf.format(calendar.time)
        fromDateDisplay = sdf1.format(calendar.time)
        setDateRangePickerTitle()

        val isAdminType = adminList.contains(SessionManager.dtUserId)
        isFilterByUser = if (isAdminType) 0 else 1

        val requestBody = ComplainListRequest(fromDate, toDate, SessionManager.dtUserId, isFilterByUser, isIssueSolved.toString())
        fetchComplainList(0,requestBody)
    }

    private fun initClickLister() {
        binding?.leaveFromFloatingButton?.setOnClickListener {
            goToAddComplainFragment()
        }

        binding?.dateRangePicker?.setOnClickListener {
            dateRangePicker()
        }
        dataAdapter.onUpdateBtnClick={ model, _ ->
            val bundle = bundleOf(
                "orderId" to model.orderId,
                "answerBy" to model.answerBy,
                "assignedTo" to model.assignedTo,
                "fullName" to model.fullName,
                "comments" to model.comments,
                "complainType" to model.complainType,
                "status" to status
            )
                findNavController().navigate(R.id.action_nav_dt_complain_list_to_updateDtComplain,bundle)
        }

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        //isIssueSolved = 0
                        callApi(0)
                    }
                    1 -> {
                        //isIssueSolved = 9
                        callApi(9)
                    }
                }
            }
        })

        dataAdapter.onItemClick = { model, _ ->
            if (model.orderId == 0) {
                context?.toast("DT order code is not valid")
            } else {
                goToComplainHistoryBottomSheet(model.orderId, model.complaintDate ?: "")
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner, { state ->
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

    private fun fetchComplainList(isIssueSolved:Int,requestBody: ComplainListRequest) {
        viewModel.fetchComplainList(requestBody).observe(viewLifecycleOwner, { list ->
            status = if (isIssueSolved == 0){
                "Pending"
            }else{
                "Solved"
            }
            dataAdapter.initLoad(list, status, isIssueSolved)
            binding?.emptyView?.isVisible = list.isEmpty()
        })
    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select date range")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {

            fromDate = sdf.format(it.first)
            toDate = sdf.format(it.second)
            fromDateDisplay = sdf1.format(it.first)
            toDateDisplay = sdf1.format(it.second)
            setDateRangePickerTitle()
            if (status=="Pending"){
                callApi(0)
            }else{
                callApi(9)
            }
        }
    }

    private fun callApi(isIssueSolved:Int){
        val requestBody = ComplainListRequest(fromDate, toDate, SessionManager.dtUserId, isFilterByUser, isIssueSolved.toString())
        fetchComplainList(isIssueSolved,requestBody)
    }

    private fun setDateRangePickerTitle() {
        //val msg = "${DigitConverter.toBanglaDate(fromDate, "yyyy-MM-dd")} - ${DigitConverter.toBanglaDate(toDate, "yyyy-MM-dd")}"
        val msg = "$fromDateDisplay - $toDateDisplay"
        binding?.dateRangePicker?.text = msg
    }

    private fun goToAddComplainFragment() {
        findNavController().navigate(R.id.nav_dt_complain)
    }

    private fun goToComplainHistoryBottomSheet(bookingCode: Int, complaintDate: String) {
        val tag = ComplainHistoryBottomSheet.tag
        val dialog = ComplainHistoryBottomSheet.newInstance(bookingCode, complaintDate)
        dialog.show(childFragmentManager, tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}