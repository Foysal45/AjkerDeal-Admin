package com.ajkerdeal.app.ajkerdealadmin.ui.employee_leave

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMDataModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMNotification
import com.ajkerdeal.app.ajkerdealadmin.api.models.fcm.FCMRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveListRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveListsItem
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveStatusUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.leave_recommend.LeaveRecommendStatusUpdateReq
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLeaveStatementBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.text.SimpleDateFormat

class LeaveStatementFragment : Fragment() {
    private var binding: FragmentLeaveStatementBinding? = null
    private val viewModel: LeaveApplicationViewModel by inject()
    private val viewModelSendPushNotification: ChatComposeViewModel by inject()
    private var dataAdapter: LeaveStatementAdapter = LeaveStatementAdapter()
    private val dataList: MutableList<LeaveListsItem> = mutableListOf()
    private var dataListCopy: MutableList<LeaveListsItem> = mutableListOf()
    private val filteredDataList: MutableList<LeaveListsItem> = mutableListOf()
    private var selectedFilter = 0
    private var selectedFilterDepartment = ""
    private var handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null
    private var firebaseWebApiKey: String? = BuildConfig.FirebaseWebApiKey
    private var selectedTab = 0

    companion object {
        fun newInstance(): LeaveStatementFragment = LeaveStatementFragment().apply {}
        val tag: String = LeaveStatementFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentLeaveStatementBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        initTabLayout()
        initClickLister()
        fetchEmployeeLeaveReport()
        fetchEmployeeLeaveCount(SessionManager.userId)
    }

    private fun initTabLayout() {
        if (selectedTab == 0){
            binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(0))
        } else if (selectedTab == 1){
            binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(1))
        }
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when(tab?.position){

                    0 -> {
                        selectedTab = 0
                        fetchEmployeeLeaveReport()
                    }

                    1 -> {
                        selectedTab = 1
                        fetchEmployeeLeaveReport()
                    }

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun initView() {
        binding?.recycleView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
        //setUpSpinner()
    }

    private fun initClickLister() {
        binding?.leaveFromFloatingButton?.setOnClickListener {
            goToLeaveApplicationFragment()

        }
        // Recommend & Not Recommend
        dataAdapter.onRecommendClick = { model, position ->
            recommendStatusUpdate(model, true, position)
        }
        dataAdapter.onRecommendRejectedClick = { model, position ->
            recommendStatusUpdate(model, false, position)
        }
        //Approve & Reject
        dataAdapter.onApprovedClick = { model, status, pos ->
            leaveStatusUpdate(model, status, pos)
        }
        dataAdapter.onRejectedClick = { model, status, pos ->
            leaveStatusUpdate(model, status, pos)
        }
        //manageSearch()
        binding?.swipeRefresh?.setOnRefreshListener {
            binding?.swipeRefresh?.isRefreshing = true
            fetchEmployeeLeaveReport()
        }
    }

    private fun leaveStatusUpdate(model: LeaveListsItem, status: String, index: Int) {
        val requestBody = LeaveStatusUpdateRequest(model.id, status, model.userId)
        viewModel.updateLeaveStatus(requestBody).observe(viewLifecycleOwner, Observer {
            if (it.status) {
                context?.toast("Status Updated")
                if (status == "Approved") {
                    sendPushNotification(model.userId ?: "", model, "Approved ${model.username}: ${model.leaveType}", true, isFromDirector = true, approveDirector = true)
                } else if (status == "Rejected") {
                    sendPushNotification(model.userId!!, model, "Rejected ${model.username}: ${model.leaveType}", true, isFromDirector = false, approveDirector = false)
                }
            }
            dataAdapter.removeItem(index)
        })
    }

    private fun recommendStatusUpdate(model: LeaveListsItem, isRecommended: Boolean, index: Int) {
        val requestBody = LeaveRecommendStatusUpdateReq(model.id, isRecommended, model.userId ?: "")
        viewModel.recommendStatusUpdate(requestBody).observe(viewLifecycleOwner, Observer {
            if (it.status) {
                context?.toast("Status Updated")
                if (isRecommended) {
                    sendPushNotification(model.userId!!, model, "${model.username}: ${model.leaveType}", true, isFromGM = true, recommendGM = true) // send push notification to the applied user
                    sendPushNotification("1", model, "${model.username}: ${model.leaveType}", true, isFromGM = false, recommendGM = true) //  send push notification t0 Director
                } else {
                    sendPushNotification(model.userId!!, model, "${model.username}: ${model.leaveType}", true, isFromGM = true, recommendGM = false) // send push notification to the applied user
                }
                dataAdapter.removeItem(index)
            }
        })
    }

    private fun sendPushNotification(
        userId: String,
        model: LeaveListsItem,
        title1: String,
        isForApplier: Boolean = false,
        isFromGM: Boolean = false,
        isFromDirector: Boolean = false,
        recommendGM: Boolean = false,
        approveDirector: Boolean = false,
        rejectDirector: Boolean = false
    ) {
        viewModel.userfirebasetoken(userId.toInt() ?: 0).observe(viewLifecycleOwner, { token ->
            if (!(token == null || firebaseWebApiKey.isNullOrEmpty())) {
                val title = "$title1 Application"
                val body = "Type:${model.leaveType} \n" +
                        "Name: ${model.name} \n" +
                        "Dept: ${model.department} \n" +
                        "Reason: ${model.reason}"
                val bodyRecommended = "Recommended"
                val bodyRejected = "Rejected"
                val bodyAccepted = "Accepted"
                var sendingBody = ""

                if (isForApplier){
                    if(isFromGM){
                        sendingBody = if(recommendGM){
                            bodyRecommended
                        }else{
                            bodyRejected
                        }
                    }
                    else if(isFromDirector){
                        if(approveDirector){
                            sendingBody = bodyAccepted
                        }else if(rejectDirector){
                            sendingBody = bodyRejected
                        }
                    }
                }else{
                    sendingBody  = body
                }

                val notificationModel = FCMNotification(
                    title, sendingBody, "", ""
                )
                val requestBody = FCMRequest(token.firebasetoken!!, notificationModel, data = FCMDataModel(
                    notificationType = "Leave",
                    title = title, body = sendingBody, receiverId = userId))
                viewModelSendPushNotification.sendPushNotifications(firebaseWebApiKey!!, requestBody)
            }
        })
    }

    private fun fetchEmployeeLeaveReport() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.recycleView?.visibility = View.GONE
        val requestBody = LeaveListRequest(SessionManager.userId.toString(), SessionManager.department, SessionManager.role)
        viewModel.fetchEmployeeLeaveSessionLists(requestBody).observe(viewLifecycleOwner, Observer { list ->
            binding?.progressBar?.visibility = View.GONE
            binding?.swipeRefresh?.isRefreshing = false
            if (list.isNullOrEmpty()) {
                binding?.emptyView?.isVisible = true
            } else {

                var filteredList = listOf<LeaveListsItem>()

                dataList.clear()
                dataList.addAll(list)

                if (selectedTab==0){
                    filteredList = dataList.filter { model->
                        model.status == "Pending"
                    }
                    Timber.d("filteredList $filteredList ")
                    if(SessionManager.role == "Managing Director"){
                        val mdFilteredList = filteredList.filter { model ->
                            model.recommended
                        }
                        filteredList = mdFilteredList
                    }
                    Timber.d("filteredList 2  $filteredList ")
                    dataAdapter.initLoad(sortDate(filteredList))
                } else {
                    filteredList = dataList.filter { model->
                        model.status != "Pending"
                    }
                    dataAdapter.initLoad(sortDate(filteredList))
                }
                selectedFilter = 0
                //setUpSpinner()
                binding?.emptyView?.isVisible = dataList.isEmpty()
            }
            binding?.recycleView?.visibility = View.VISIBLE
        })
        //setUpSpinner()
    }
    private fun sortDate(leaveListsItem:  List<LeaveListsItem>) =
        leaveListsItem.sortedByDescending { data ->
            SimpleDateFormat("yyyy-MM-dd").parse(data.date).time

    }

    @SuppressLint("SetTextI18n")
    private fun fetchEmployeeLeaveCount(userId: Int) {
        viewModel.fetchEmployeeLeaveCount(userId).observe(viewLifecycleOwner, Observer { model ->
            if (!model.data.isNullOrEmpty()) {
                binding?.leaveCountLayout?.visibility = View.VISIBLE
                binding?.sickLeaveCount?.text = "${model.data?.first()?.sickLeave ?: 0}/14"
                binding?.casualLeaveCount?.text = "${model.data?.first()?.casualLeave ?: 0}/10"
            } else {
                binding?.leaveCountLayout?.visibility = View.GONE
            }

        })

    }

    /*private fun setUpSpinner() {

        val filterList: MutableList<String> = mutableListOf()
        filterList.add("All")
        filterList.add("Emerging")
        filterList.add("VIP")
        filterList.add("Manager")
        filterList.add("Sales")
        filterList.add("CRM")
        filterList.add("Fulfillment")
        filterList.add("Accounts")
        filterList.add("Social")
        filterList.add("Content")
        filterList.add("IT")
        filterList.add("Merchandising")
        filterList.add("Service Quality Analyst")

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

    private fun filterReport() {
        binding?.progressBar?.visibility = View.VISIBLE
        if (!dataList.isNullOrEmpty()) {
            binding?.progressBar?.visibility = View.GONE
            when (selectedFilter) {
                0 -> {
                    filteredDataList.clear()
                    filteredDataList.addAll(dataList)
                    dataAdapter.initLoad(filteredDataList)
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
                5 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                6 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                7 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                8 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                9 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                10 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                11 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
                12 -> {
                    filterDepartmentWise(selectedFilterDepartment)
                }
            }
            binding?.recycleView?.visibility = View.VISIBLE
        }
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
            (binding?.recycleView?.adapter as LeaveStatementAdapter).initLoad(dataListCopy)
            binding?.progressBar?.visibility = View.GONE
            return
        }
        val filteredList = dataListCopy.filter { model ->
            (model.name!!.contains(searchKey, true))
        }
        (binding?.recycleView?.adapter as LeaveStatementAdapter).initLoad(filteredList)
        binding?.progressBar?.visibility = View.GONE
    }

    private fun filterDepartmentWise(checkDept: String) {

        val filteredList = dataList.filter { data ->
            data.department == checkDept
        }
        filteredDataList.clear()
        filteredDataList.addAll(filteredList)
        dataAdapter.initLoad(filteredList)
    }*/

    private fun goToLeaveApplicationFragment() {
        findNavController().navigate(R.id.nav_leave_application_form)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
