package com.ajkerdeal.app.ajkerdealadmin.ui.retention.all_orders

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.GenericResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CODReqBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CODResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CourierOrderViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeActivity
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.all_orders.AllOrdersAdapter
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.all_orders.details_bottomsheet.AllOrdersDetailsBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter
import com.ajkerdeal.app.ajkerdealadmin.utils.callHelplineNumber
import timber.log.Timber

class AllOrdersFragment : Fragment() {

    private lateinit var rvAllOrder: RecyclerView
    private lateinit var tvTotalOrder: TextView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var ivEmpty: ImageView

    private lateinit var allOrderProgressBar: ProgressBar

    private lateinit var dateRangePicker: TextView


    private lateinit var allOrdersAdapter: AllOrdersAdapter

    private var isLoading = false
    private var totalLoadedData = 0
    private var layoutPosition = 0
    private var totalCount = 0
    private var courierOrderViewModelList: MutableList<CourierOrderViewModel> = mutableListOf()
    private var defaultFromDate = "2001-01-01"
    private var defaultToDate = "2001-01-01"
    private var fromDate = "2001-01-01"
    private var toDate = "2001-01-01"
    private var status = -1
    private var statusGroup = "-1"
    private var orderId = ""
    private var mobileNumber = ""
    private var collectionName = ""
    private var orderTypeFilter = ""
    private var isMoreDataAvailable = true
    private val statusList: MutableList<Int> = mutableListOf(-1)
    private val statusGroupList: MutableList<String> = mutableListOf("-1")
    private var shouldOpenFilter: Boolean = false
    private var collectionAmount = 0
    private var paymentInProcessing = 0
    private var paymentPaid = 0
    private var paymentReady = 0
    private var courierUserId = -1

    private var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var isFromDashBoard: Boolean = false

    private val viewModel: AllOrderViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAllOrder = view.findViewById(R.id.rvAllOrder)
        allOrderProgressBar = view.findViewById(R.id.allOrderProgressBar)
        //tvTotalOrder = view.findViewById(R.id.tvTotalOrder)
        //allOrderFilterLay = view.findViewById(R.id.allOrderFilterLay)
        ivEmpty = view.findViewById(R.id.ivEmpty)

        dateRangePicker = view.findViewById(R.id.dateRangePicker)

        //Bundle
        shouldOpenFilter = arguments?.getBoolean("shouldOpenFilter", false) ?: false
        isFromDashBoard = arguments?.getBoolean("isFromDashBoard", false) ?: false
        statusGroup = arguments?.getString("statusGroup", "-1") ?: "-1"
        fromDate = arguments?.getString("fromDate", defaultFromDate) ?: defaultFromDate
        toDate = arguments?.getString("toDate", defaultToDate) ?: defaultToDate
        defaultFromDate = fromDate
        defaultToDate = toDate
        val dashboardStatusFilter = arguments?.getString("dashboardStatusFilter", "-1") ?: "-1"
        courierUserId = arguments?.getInt("courierUserId", -1) ?: -1
        val statusArray = dashboardStatusFilter.split(",")
        statusGroupList.clear()
        statusGroupList.addAll(statusArray)
        //activeFilter(true)



        linearLayoutManager = LinearLayoutManager(context)

        allOrdersAdapter = AllOrdersAdapter(requireContext(), courierOrderViewModelList)
        allOrdersAdapter.isFromDashBoard = isFromDashBoard
        rvAllOrder.apply {
            layoutManager = linearLayoutManager
            adapter = allOrdersAdapter
            //addItemDecoration(DividerItemDecoration(rvAllOrder.getContext(), DividerItemDecoration.VERTICAL))
        }


        getAllOrders(0, 20)

        rvAllOrder.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutPosition = linearLayoutManager.findLastVisibleItemPosition()
                //Timber.e("layoutPosition", layoutPosition.toString() + " " + totalLoadedData + " " + isLoading + " " + totalCount + " " + isMoreDataAvailable)
                if (dy > 0) {
                    if (layoutPosition >= (totalLoadedData - 2) && !isLoading && layoutPosition < totalCount && isMoreDataAvailable) {
                        getAllOrders(totalLoadedData, 20)
                        //Timber.e("layoutPosition loadMoreCalled ", layoutPosition.toString() + " " + totalLoadedData + " " + isLoading + " " + totalCount)
                    }
                }
            }
        })

        allOrdersAdapter.onItemClick = { model, position ->
            showAllOrdersDetailsBottomSheet(model)
        }

        allOrdersAdapter.onCallClicked = { model, position ->
            var number = model?.courierAddressContactInfo?.mobile
            var altNumber = model?.courierAddressContactInfo?.otherMobile

            if (!number.isNullOrEmpty() && !altNumber.isNullOrEmpty()) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("কোন নাম্বার এ কল করতে চান")
                val numberLists = arrayOf(number, altNumber)
                builder.setItems(numberLists) { _, which ->
                    when (which) {
                        0 -> {
                            callHelplineNumber(numberLists[0])
                        }
                        1 -> {
                            callHelplineNumber(numberLists[1])
                        }
                    }
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                callHelplineNumber(number!!)
            }
        }

        dateRangePicker.setOnClickListener {
            dateRangePicker()
        }

    }


    private fun showAllOrdersDetailsBottomSheet(model: CourierOrderViewModel) {
        val tag: String = AllOrdersDetailsBottomSheet.tag
        val dialog: AllOrdersDetailsBottomSheet = AllOrdersDetailsBottomSheet.newInstance(model)
        dialog.show(childFragmentManager, tag)

    }

    private fun dateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("ডেট রেঞ্জ সিলেক্ট করুন")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {

            fromDate = sdf.format(it.first)
            toDate = sdf.format(it.second)

            //activeFilter()
            courierOrderViewModelList.clear()
            allOrdersAdapter.notifyDataSetChanged()
            getAllOrders(0, 20)

        }
    }

    private fun getAllOrders(index: Int, count: Int) {

        isLoading = true
        allOrderProgressBar.visibility = View.VISIBLE
        if (courierUserId!=-1){
            val reqModel = CODReqBody(status, statusList, statusGroupList, fromDate, toDate, orderTypeFilter, courierUserId,
                "", orderId, collectionName, mobileNumber, index, count)

            viewModel.getAllOrders(reqModel).observe(viewLifecycleOwner, {  model ->
                if (allOrderProgressBar.visibility == View.VISIBLE) {
                    allOrderProgressBar.visibility = View.GONE
                }
                isLoading = false
                //Timber.e("getAllOrdersResponse", " s " + response.body()!!.model.courierOrderViewModel)
                collectionAmount = model.adTotalCollectionAmount?.toInt() ?: 0
                paymentInProcessing = model.adCourierPaymentInfo?.paymentInProcessing?.toInt() ?: 0
                paymentPaid = model.adCourierPaymentInfo?.paymentPaid?.toInt() ?: 0
                paymentReady = model.adCourierPaymentInfo?.paymentReady?.toInt() ?: 0

                if (index == 0) {
                    courierOrderViewModelList.clear()
                }
                courierOrderViewModelList.addAll(model.courierOrderViewModel!!)
                totalLoadedData = courierOrderViewModelList.size

                allOrdersAdapter.notifyDataSetChanged()
                isMoreDataAvailable = model.courierOrderViewModel!!.size >= count - 2
                //Timber.e("getAllOrdersResponse", " s " + response.body().toString())

                if (index < 20) {
                    totalCount = model.totalCount!!.toInt()
                    val msg = "মোট পার্সেলঃ <font color='#CC000000'><b>${DigitConverter.toBanglaDigit(totalCount)}</b></font> টি"
                    //tvTotalOrder.text = HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }

                if(totalLoadedData == 0){
                    ivEmpty.visibility = View.VISIBLE
                } else {
                    ivEmpty.visibility = View.GONE
                }


            })
        }



    }

}
