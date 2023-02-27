package com.ajkerdeal.app.ajkerdealadmin.ui.instant_payment_information

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPaymentModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPymentRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentInstantPaymentInformationBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.squareup.okhttp.Call
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class InstantPaymentInformationFragment : Fragment() {

    private var binding: FragmentInstantPaymentInformationBinding? = null
    private val viewModel: InstantPaymentViewModel by inject()

    private var dataAdapter: InstantPaymentAdapter = InstantPaymentAdapter()

    private var fromDate = ""
    private var preferredPaymentCycle = ""
    private var displayFromDate = ""
    private var displayToDate = ""
    private var toDate = ""
    private var currentDate = ""
    private var displayCurrentDate = ""
    private var progressBar: ProgressBar? = null
    private var selectedFilter = 0
    private var filter: Boolean = false
    private var flag: Int = 0

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM ", Locale.US)

    private var dataList: MutableList<InstantPaymentModel> = mutableListOf()
    private var dataListCopy: MutableList<InstantPaymentModel> = mutableListOf()
    private var handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    companion object {
        fun newInstance(): InstantPaymentInformationFragment = InstantPaymentInformationFragment().apply {}
        val tag: String = InstantPaymentInformationFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Instant Payment"
        return FragmentInstantPaymentInformationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.progress_bar)
        initView()
        initClickLister()
    }

    private fun initView() {

        setUpSpinner()


        with(binding?.instantPaymentInformationRecyclerView!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = dataAdapter
        }

        initViewWithData()
        manageSearch()

        viewModel.viewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer { state ->
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

    private fun initViewWithData(){
        val calendar = Calendar.getInstance()
        preferredPaymentCycle = "week"
        currentDate = sdf1.format(calendar.time)
        displayCurrentDate = sdf1.format(calendar.time)
        fromDate = sdf1.format(calendar.time)
        displayFromDate = sdf1.format(calendar.time)
        toDate = sdf1.format(calendar.time)
        displayToDate = sdf1.format(calendar.time)
        setDateRangePickerTitle()
        fetchInstantPaymentData(fromDate, toDate, preferredPaymentCycle)
    }

    private fun initClickLister() {
        binding?.datePickerInstantPaymentInformation?.setOnClickListener {
            dateRangePicker()
        }
        dataAdapter.onMobileNumberClick = { mobileNumber ->
            callHelplineNumber(mobileNumber.mobile)
        }

    }

    private fun manageSearch() {

        binding?.searchETInstantPayment?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    val searchKey = p0.toString()
                    search(searchKey)
                }
                handler.postDelayed(workRunnable!!, 500L)
            }
        })

        binding?.searchBtnInstantPayment?.setOnClickListener {
            hideKeyboard()
            val searchKey = binding?.searchETInstantPayment?.text?.toString() ?: ""
            search(searchKey)
        }
    }

    private fun search(searchKey: String) {

        progressBar?.visibility = View.VISIBLE
        if (dataListCopy.isEmpty()) {
            dataListCopy.clear()
            dataListCopy.addAll(dataList)
        }
        if (searchKey.isEmpty()) {
            (binding?.instantPaymentInformationRecyclerView?.adapter as InstantPaymentAdapter).initLoad(dataListCopy)
            progressBar?.visibility = View.GONE
            return
        }
        val filteredList = dataListCopy.filter { model ->
            (model.companyName.contains(searchKey, true))
        }
        (binding?.instantPaymentInformationRecyclerView?.adapter as InstantPaymentAdapter).initLoad(filteredList)
        progressBar?.visibility = View.GONE
    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select Date Range")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            fromDate = sdf1.format(it.first)
            displayFromDate = sdf1.format(it.first)
            toDate = sdf1.format(it.second)
            displayToDate = sdf1.format(it.second)
            setDateRangePickerTitle()
            fetchInstantPaymentData(fromDate, toDate, preferredPaymentCycle)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDateRangePickerTitle() {
        binding?.datePickerInstantPaymentInformation?.text = "$fromDate - $toDate"
    }

    private fun setUpSpinner() {

        val pickupPreferredPaymentCycle: MutableList<String> = mutableListOf()
        pickupPreferredPaymentCycle.add("week")
        pickupPreferredPaymentCycle.add("instant")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupPreferredPaymentCycle)
        binding?.filterSpinner?.adapter = spinnerAdapter
        binding?.filterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFilter = position
                preferredPaymentCycle = pickupPreferredPaymentCycle[position]
                filter = true
                if (position == 0) {
                    flag = 0

                    fetchInstantPaymentData(fromDate,toDate,preferredPaymentCycle)
                } else if (position == 1){
                    flag = 1
                    fetchInstantPaymentData(fromDate,toDate,preferredPaymentCycle)
                }
            }
        }

    }

    private fun fetchInstantPaymentData(fromDate: String, toDate: String, preferredPaymentCycle: String) {
        val requestBody = InstantPymentRequest(fromDate, preferredPaymentCycle, toDate)
        viewModel.getInstantPaymentInformationList(requestBody).observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            dataAdapter.initLoad(list)
            binding?.emptyView?.isVisible = list.isEmpty()

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}