package com.ajkerdeal.app.ajkerdealadmin.ui.order_report.merchant_order_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMerchantOrderDetailsBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.order_report.order.OrderReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class MerchantOrderDetailsFragment : Fragment() {
    private var binding: FragmentMerchantOrderDetailsBinding? = null
    private  var dataAdapter: MerchantOrderDetailsAdapter = MerchantOrderDetailsAdapter()
    private val viewModel: OrderReportViewModel by inject()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM, yyyy", Locale.US)

    private var selectedDate = "2001-01-01"


    companion object {
        fun newInstance(): MerchantOrderDetailsFragment = MerchantOrderDetailsFragment().apply {}
        val tag: String = MerchantOrderDetailsFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentMerchantOrderDetailsBinding.inflate(inflater, container, false).also {
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
        fetchEmployeeWorkingReport(selectedDate)
    }

    private fun fetchEmployeeWorkingReport(date: String){
        viewModel.fetchEmployeeWorkingReport(date).observe(viewLifecycleOwner, Observer { list->
            dataAdapter.initLoad(list)
            binding?.emptyView?.isVisible = list.isEmpty()
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}