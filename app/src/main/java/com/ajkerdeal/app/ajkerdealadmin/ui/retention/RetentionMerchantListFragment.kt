package com.ajkerdeal.app.ajkerdealadmin.ui.retention

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.hub.HubInfo
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.OrderWiseRetentionMerchantListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchantListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentRetentionManagerListBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewComplainPopupBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey.LoanSurveryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet.CallInfoBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet.ComplainAddBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet.status_break_down.StatusBreakDownBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet.VisitInfoBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.quick_booking.QuickBookingBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class RetentionManagerListFragment : Fragment() {
    private var binding: FragmentRetentionManagerListBinding? = null
    private var dataAdapter: RetentionMerchantListAdapter = RetentionMerchantListAdapter()
    private val returnDataList: MutableList<OrderCountResponse> = mutableListOf()

    private val viewModel: RetentionMerchantListViewModel by inject()
    private val viewModel2: LoanSurveryViewModel by inject()

    // Paging params
    private var isLoading = false
    private val visibleThreshold = 5

    private var callInfoModel: RetentionMerchentListModel? = null
    private var calledMerchantIndex: Int = 0

    private val calenderNow = Calendar.getInstance()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var dayOfYear = 0
    private var today = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedStartDate = ""
    private var selectedEndDate = ""
    private var currentDate = ""

    private var selectedFilter = 0
    private var filter: Boolean = false
    private var flag: Int = 0

    companion object {
        fun newInstance(): RetentionManagerListFragment = RetentionManagerListFragment().apply {}
        val tag: String = RetentionManagerListFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRetentionManagerListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        initClickLister()
        fetchRetentionMerchant(0)

    }

    private fun initView() {

        setUpSpinner()
        calculateDuration()

        binding?.recycleView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }

    private fun initClickLister(){

        binding?.searchBtn?.setOnClickListener {
            hideKeyboard()
            fetchRetentionMerchant(0)
        }

        binding?.searchET?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                fetchRetentionMerchant(0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding?.closeBtn?.setOnClickListener {
            binding?.searchET?.text?.clear()
            fetchRetentionMerchant(0)
        }

        dataAdapter.onLoanApplyClicked = { model, position ->
            val progressDialog = progressDialog()
            progressDialog.show()
            model.companyName == null
            progressDialog.setCancelable(false)
            viewModel2.getLoanSurveyData(model.courierUserId).observe(viewLifecycleOwner, {
                progressDialog.dismiss()
                if(it.isEmpty()){
                    val bundle = bundleOf(
                        "courierUserId" to model.courierUserId,
                        "mobileNumber" to model.mobile,
                        "userPassword" to model.password,
                        "companyName" to model.companyName,
                        "hasData" to false
                    )
                    findNavController().navigate(R.id.action_nav_retention_merchant_to_loanSurveyFragment, bundle)
                }else{
                    val bundle = bundleOf(
                        "courierUserId" to model.courierUserId,
                        "mobileNumber" to model.mobile,
                        "userPassword" to model.password,
                        "hasData" to true
                    )
                    findNavController().navigate(R.id.action_nav_retention_merchant_to_loanSurveyFragment, bundle)
                }
            })
        }

        dataAdapter.onCallClicked = { model, position->
            val mobile = model.mobile ?: ""
            val alterMobile = model.alterMobile ?: ""
            val bkashNumber = model.bkashNumber ?: ""
            val numberLists = mutableListOf<String>()
            val numberTypeLists = mutableListOf<String>()
            if (mobile.isEmpty()) {
                context?.toast("No mobile number found")
            } else {
                numberLists.add(mobile)
                numberTypeLists.add("$mobile (Registration)")
                if (alterMobile.isNotEmpty() && alterMobile.length == 11) {
                    numberLists.add(alterMobile)
                    numberTypeLists.add("$alterMobile (Alternate)")
                }
                if (bkashNumber.isNotEmpty() && bkashNumber.length == 11) {
                    numberLists.add(bkashNumber)
                    numberTypeLists.add("$bkashNumber (Bkash)")
                }
            }
            if (numberLists.size > 1) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Select number to call")
                builder.setItems(numberTypeLists.toTypedArray()) { _, which ->
                    callHelplineNumber(numberLists[which])
                }
                val dialog = builder.create()
                dialog.show()
            } else if (numberLists.size == 1) {
                callHelplineNumber(numberLists.first())
            }
            callInfoModel = model
            calledMerchantIndex = position
        }
        dataAdapter.onCallInfoClicked = { model, position ->
            showCalledInfoBottomSheet(model)
        }
        dataAdapter.onVisitInfoClicked = { model, position ->
            showVisitedInfoBottomSheet(model)
        }
        dataAdapter.onComplainAddClicked = { model, position ->
            showPopUp(model)
        }
        dataAdapter.onQuickBookingClicked = { model, position ->
            showQuickOrderBottomSheet(model.courierUserId)
        }

        dataAdapter.onReturnClicked = { model, position ->
            fetchReturnCount(model)
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

        viewModel.pagingState.observe(viewLifecycleOwner, Observer { state ->
            isLoading = false
            if (state.isInitLoad) {
                dataAdapter.initLoad(state.dataList)

                if (state.dataList.isEmpty()) {
                    binding?.emptyView?.visibility = View.VISIBLE
                } else {
                    binding?.emptyView?.visibility = View.GONE
                }

            } else {
                dataAdapter.pagingLoad(state.dataList)
                if (state.dataList.isEmpty()) {
                    isLoading = true
                }
            }
        })

        binding?.recycleView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val currentItemCount = (recyclerView.layoutManager as LinearLayoutManager).itemCount
                    val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (!isLoading && currentItemCount <= lastVisibleItem + visibleThreshold) {
                        isLoading = true
                        fetchRetentionMerchant(currentItemCount)
                    }
                }

            }
        })
    }

    private fun calculateDuration() {

        viewModel.getDataDuration().observe(viewLifecycleOwner, {

            currentYear = calenderNow.get(Calendar.YEAR)
            dayOfYear = calenderNow.get(Calendar.DAY_OF_YEAR)
            today = calenderNow.get(Calendar.DAY_OF_MONTH)
            currentMonth = calenderNow.get(Calendar.MONTH)
            val lastActualDay = calenderNow.getActualMaximum(Calendar.DAY_OF_MONTH)
            currentDate = sdf.format(calenderNow.timeInMillis)

            selectedYear = currentYear
            selectedMonth = currentMonth + 1

            selectedEndDate = "$selectedYear-$selectedMonth-$today"

            val calendarStart = Calendar.getInstance()
            calendarStart.add(Calendar.MONTH, it.dashboardDataDuration * -1)
            val startYear = calendarStart.get(Calendar.YEAR)
            val startMonth = calendarStart.get(Calendar.MONTH) + 1
            selectedStartDate = "$startYear-$startMonth-01"

        })

    }

    private fun fetchReturnCount(model1: RetentionMerchentListModel) {
        val progressDialog = progressDialog()
        progressDialog.show()
        progressDialog.setCancelable(false)
        if (!selectedStartDate.isNullOrEmpty() || !selectedEndDate.isNullOrEmpty()){
            val reqBody = OrderCountReqBody(0,0, selectedStartDate, selectedEndDate, model1.courierUserId)
            Timber.d("reqBodyBal $reqBody")
            viewModel.getOrderCount(reqBody).observe(viewLifecycleOwner, { orderCountList ->

                progressDialog.dismiss()

                val dashboardList: MutableList<OrderCountResponse> = mutableListOf()
                returnDataList.clear()
                var returnCount = 0

                orderCountList.forEach { model ->
                    when(model.statusGroupId){
                        9, 10, 11 -> {
                            returnCount += model.count ?: 0
                            returnDataList.add(model)
                        }
                    }
                }

                val returnData = OrderCountResponse(
                    name = "রিটার্নে আছে",
                    dashboardSpanCount = 1,
                    statusGroupId = 9,
                    count = returnCount,
                    dashboardViewColorType = "negative",
                    dashboardRouteUrl = "return",
                    dashboardCountSumView = "count"
                )

                dashboardList.clear()
                dashboardList.add(returnData)

                showStatusSubGroupBottomSheet(returnData, model1)

            })
        }
    }

    private fun showStatusSubGroupBottomSheet(model2: OrderCountResponse, model1: RetentionMerchentListModel) {

        val dataList: MutableList<OrderCountResponse>
        var title = ""
        when (model2.dashboardRouteUrl) {

            "return" -> {
                dataList = returnDataList
                title = "রিটার্নে আছে"
            }
            else -> return
        }

        val tag = StatusBreakDownBottomSheet.tag
        val dialog = StatusBreakDownBottomSheet.newInstance(title, dataList)
        dialog.show(childFragmentManager, tag)
        dialog.onItemClicked = { model, position ->
            dialog.dismiss()
            if (model.count > 0) {
                goToAllOrder(model.name ?: "", model.dashboardStatusFilter, selectedStartDate, selectedEndDate,model1)
                //UserLogger.logGenie("Dashboard_AllOrder_${model.statusGroupId}")
            } else {
                context?.toast("পর্যাপ্ত তথ্য নেই")
            }
        }
        /*dialog.onMapClick = { model, position ->
            if (model.statusGroupId == 11) {
                dialog.dismiss()
                goToNearByHubMap()
            } else if (model.statusGroupId == 10) {
                dialog.dismiss()
                val hubModel = HubInfo(10, "সেন্ট্রাল হাব", "lalmatia-hub", true, "7/7 Block C Lalmatia Mohammadpur", "90.3678406", "23.7544619", "01521427957")
                goToHubMap(hubModel)
            }
        }*/
    }

    private fun goToAllOrder(statusGroupName: String, statusFilter: String, startDate: String, endDate: String, model1: RetentionMerchentListModel) {

        val bundle = bundleOf(
            "statusGroup" to statusGroupName,
            "fromDate" to startDate,
            "toDate" to endDate,
            "dashboardStatusFilter" to statusFilter,
            "isFromDashBoard" to true,
            "courierUserId" to model1.courierUserId
        )

        findNavController().navigate(R.id.action_nav_retention_merchant_to_allOrdersFragment, bundle)

        /*val fragment = AllOrdersFragment.newInstance(bundle, true)
        val ft: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
        ft?.add(R.id.mainActivityContainer, fragment, AllOrdersFragment.tag)
        ft?.addToBackStack(AllOrdersFragment.tag)
        ft?.commit()*/
    }

    private fun goToNearByHubMap() {
        val bundle = bundleOf(
            "isNearByHubView" to true
        )
        //findNavController().navigate(R.id.nav_dashboard_map, bundle)
        //addFragment(MapFragment.newInstance(bundle), MapFragment.tag)
    }

    private fun goToHubMap(hubModel: HubInfo) {

        val bundle = bundleOf(
            "hubView" to true,
            "hubModel" to hubModel
        )

        //findNavController().navigate(R.id.nav_dashboard_map, bundle)

        /*val fragment = MapFragment.newInstance(bundle)
        val ft: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
        ft?.add(R.id.mainActivityContainer, fragment, MapFragment.tag)
        ft?.addToBackStack(MapFragment.tag)
        ft?.commit()*/
    }

    private fun showPopUp(model: RetentionMerchentListModel){
        var popUpBinding: ItemViewComplainPopupBinding?
        val view = ItemViewComplainPopupBinding.inflate(layoutInflater).also {
            popUpBinding = it
        }.root

        val builder = MaterialAlertDialogBuilder(requireContext())

        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.attributes?.width = (getDeviceMetrics(requireContext())?.widthPixels?.times(0.80))?.toInt()

        dialog.show()
        popUpBinding?.complainBtn?.setOnClickListener {
           showComplainAddBottomSheet(model)
            dialog.dismiss()
        }
        popUpBinding?.complainHistoryBtn?.setOnClickListener {
            val bundle = bundleOf(
                "courierUserId" to model.courierUserId,
                "mobile" to model.mobile,
                "password" to model.password
            )
            findNavController().navigate(R.id.merchantComplainListFragment, bundle)
            dialog.dismiss()
        }
    }

    private fun getDeviceMetrics(context: Context): DisplayMetrics? {
        val metrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        display.getMetrics(metrics)
        return metrics
    }

    private fun setUpSpinner() {

        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        pickupBreakReasonList.add("No Filter")
        pickupBreakReasonList.add("Top Order")
        pickupBreakReasonList.add("No Order(7 Days)")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBreakReasonList)
        binding?.filterSpinner?.adapter = spinnerAdapter
        binding?.filterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFilter = position
                filter = true
                if (position == 0) {
                    filter = false
                } else if (position == 1){
                    flag = 0
                    fetchRetentionMerchant(0)
                } else if (position == 2){
                    flag = 7
                    fetchRetentionMerchant(0)
                }
            }
        }

    }

    private fun showQuickOrderBottomSheet(merchantId: Int) {
        val tag: String = QuickBookingBottomSheet.tag
        val dialog: QuickBookingBottomSheet = QuickBookingBottomSheet.newInstance(merchantId)
        dialog.show(childFragmentManager, tag)
        dialog.onClose = {
            Handler(Looper.getMainLooper()).postDelayed({
                hideKeyboard()
            }, 200L)
        }
        dialog.onOrderPlace = { msg ->
            alert(getString(R.string.instruction), msg, true, getString(R.string.ok), "সকল কুইক বুকিং"){
                if (it == AlertDialog.BUTTON_NEGATIVE) {

                }
            }.show()
        }
    }

    private fun showCalledInfoBottomSheet(model: RetentionMerchentListModel) {
        val tag: String = CallInfoBottomSheet.tag
        val dialog: CallInfoBottomSheet = CallInfoBottomSheet.newInstance(model)
        dialog.show(childFragmentManager, tag)
    }

    private fun showVisitedInfoBottomSheet(model: RetentionMerchentListModel) {
        val tag: String = VisitInfoBottomSheet.tag
        val dialog: VisitInfoBottomSheet = VisitInfoBottomSheet.newInstance(model)
        dialog.show(childFragmentManager, tag)
    }

    private fun showComplainAddBottomSheet(model: RetentionMerchentListModel) {
        val tag: String = ComplainAddBottomSheet.tag
        val dialog: ComplainAddBottomSheet = ComplainAddBottomSheet.newInstance(model)
        dialog.show(childFragmentManager, tag)
    }

    private fun fetchRetentionMerchant(index: Int) {
        val userId = SessionManager.dtUserId
        val searchKey = binding?.searchET?.text.toString()
        //userId 289, 712 for test
        //userId = 289
        if (filter){
            val requestBody = OrderWiseRetentionMerchantListRequest(userId, index, 20, searchKey, flag)
            viewModel.getOrderWiseRetentionMerchantList(requestBody)
        }else{
            val requestBody = RetentionMerchantListRequest(userId, index, 20, searchKey)
            viewModel.getRetentionMerchantList(requestBody)
        }

    }

    override fun onResume() {
        super.onResume()
        if (callInfoModel != null){
            showCalledInfoBottomSheet(callInfoModel!!)
            callInfoModel = null
        }
    }
    private fun showAlert(message: String) {
        alert("Notice", message, false, "Ok").show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}