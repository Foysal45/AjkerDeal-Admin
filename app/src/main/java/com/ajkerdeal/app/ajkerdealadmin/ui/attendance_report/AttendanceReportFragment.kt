package com.ajkerdeal.app.ajkerdealadmin.ui.attendance_report

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.report.WorkReportData
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentAttendanceReportBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.attendance_report.details_bottomsheet.AttendanceReportDetailsDialog
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AttendanceReportFragment : Fragment() {

    private var binding: FragmentAttendanceReportBinding? = null
    private var dataAdapter: AttendanceReportAdapter = AttendanceReportAdapter()
    private val viewModel: AttendanceReportViewModel by inject()

    private val dataList: MutableList<WorkReportData> = mutableListOf()
    private val filteredDataList: MutableList<WorkReportData> = mutableListOf()
    private var dataListCopy: MutableList<WorkReportData> = mutableListOf()

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM, yyyy", Locale.US)

    private var selectedDate = "2001-01-01"
    private var selectedDateFormatted = ""

    private var selectedFilter = 0
    private var selectedFilterDepartment = ""
    private var handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    companion object {
        fun newInstance(): AttendanceReportFragment = AttendanceReportFragment().apply {}
        val tag: String = AttendanceReportFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentAttendanceReportBinding.inflate(inflater, container, false).also {
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
        setUpSpinner()
    }

    private fun initClickLister() {
        binding?.dateRangePicker?.setOnClickListener {
            dateRangePicker()
        }

        dataAdapter.onItemClick = { dataList, _ ->
            showReportDetailsBottomSheet(dataList)
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
        binding?.swipeRefresh?.setOnRefreshListener {
            fetchEmployeeWorkingReport(selectedDate)
        }

        manageSearch()
    }

    private fun manageSearch() {

        binding?.searchET?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    val searchKey = p0.toString().trim()
                    search(searchKey)
                }
                handler.postDelayed(workRunnable!!, 100L)
            }
        })

        binding?.searchBtn?.setOnClickListener {
            hideKeyboard()
            val searchKey = binding?.searchET?.text.toString()
            search(searchKey)
        }

        binding?.closeBtn?.setOnClickListener {
            binding?.searchET?.text?.clear()
        }

    }

    private fun search(searchKey: String) {

        binding?.progressBar?.visibility = View.VISIBLE
        if (dataListCopy.isEmpty()) {
            dataListCopy.clear()
            dataListCopy.addAll(filteredDataList)
        }
        if (searchKey.isEmpty()) {
            (binding?.recycleView?.adapter as AttendanceReportAdapter).initLoad(dataListCopy)
            binding?.progressBar?.visibility = View.GONE
            return
        }
        val filteredList = dataListCopy.filter { model->
            (model.userName!!.contains(searchKey, true))
        }
        (binding?.recycleView?.adapter as AttendanceReportAdapter).initLoad(filteredList)
        binding?.progressBar?.visibility = View.GONE
    }


    private fun filterReport() {
        binding?.progressBar?.visibility = View.VISIBLE
        if (!dataList.isNullOrEmpty()){
            binding?.progressBar?.visibility = View.GONE
            when (selectedFilter) {
                0 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                1 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                2 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                3 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                4 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                5-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                6-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                7-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                8-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                9-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                10-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                11-> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                12-> {
                    filterDepartmentWise(selectedFilterDepartment)
                    filteredDataList.clear()
                    filteredDataList.addAll(dataList)
                    binding?.totalAttendanceCount?.text = filteredDataList.size.toString()
                    dataAdapter.initLoad(filteredDataList)
                }
            }
            binding?.recycleView?.visibility = View.VISIBLE
        }
    }

    private fun filterDepartmentWise(checkDept: String){

        val filteredList = dataList.filter { data->
            data.session?.first()?.department == checkDept
        }
         filteredDataList.clear()
        filteredDataList.addAll(filteredList)
        binding?.totalAttendanceCount?.text = filteredDataList.size.toString()
        dataAdapter.initLoad(filteredList)
    }

    private fun setUpSpinner() {

        val filterList: MutableList<String> = mutableListOf()
        filterList.add("IT")
        filterList.add("Emerging")
        filterList.add("VIP")
        filterList.add("Manager")
        filterList.add("Sales")
        filterList.add("CRM")
        filterList.add("Fulfillment")
        filterList.add("Accounts")
        filterList.add("Social")
        filterList.add("Content")
        filterList.add("Merchandising")
        filterList.add("Service Quality Analyst")
        filterList.add("All")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, filterList)
        binding?.filterSpinner?.adapter = spinnerAdapter
        binding?.filterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFilter = position
                selectedFilterDepartment = filterList[position]
                filterReport()
                binding?.recycleView?.scrollToPosition(0)
            }
        }

    }

    private fun initPreselectedDate() {
        val date = Date()
        selectedDate = sdf.format(date)
        selectedDateFormatted = sdf1.format(date)
        setDateRangePickerTitle()
        fetchEmployeeWorkingReport(selectedDate)
    }

    private fun fetchEmployeeWorkingReport(date: String) {
        viewModel.fetchEmployeeWorkingReport(date).observe(viewLifecycleOwner, Observer { list ->
            binding?.swipeRefresh?.isRefreshing = false
            binding?.totalAttendanceCount?.text = list.size.toString()
            val sortedList = list.sortedWith(
                compareBy<WorkReportData>{ it?.session?.get(0)?.startTime?.subSequence(0, 2).toString().toInt() }.
                    thenBy { it?.session?.get(0)?.startTime?.subSequence(3, 5).toString().toInt() }.
                    thenBy { it?.session?.get(0)?.startTime?.subSequence(6, 8).toString().toInt() }
                )
            dataList.clear()
            dataList.addAll(sortedList)
            dataAdapter.initLoad(dataList)
            selectedFilter = 0
            setUpSpinner()
            binding?.emptyView?.isVisible = dataList.isEmpty()
        })
    }

    private fun showReportDetailsBottomSheet(data: WorkReportData) {
        val tag: String = AttendanceReportDetailsDialog.tag
        val dialog: AttendanceReportDetailsDialog = AttendanceReportDetailsDialog.newInstance(data)
        dialog.show(childFragmentManager, tag)
    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select date")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            selectedDate = sdf.format(it)
            selectedDateFormatted = sdf1.format(it)
            setDateRangePickerTitle()
            fetchEmployeeWorkingReport(selectedDate)
        }
    }


    private fun setDateRangePickerTitle() {
        binding?.dateRangePicker?.text = selectedDateFormatted
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
