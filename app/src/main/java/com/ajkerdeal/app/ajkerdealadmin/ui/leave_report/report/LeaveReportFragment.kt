package com.ajkerdeal.app.ajkerdealadmin.ui.leave_report.report

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
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.LeaveData
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLeaveReportBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber

class LeaveReportFragment : Fragment() {
    private var binding: FragmentLeaveReportBinding? = null
    private  var dataAdapter: LeaveReportAdapter = LeaveReportAdapter()
    private val viewModel: LeaveReportViewModel by inject()

    companion object {
        fun newInstance(): LeaveReportFragment = LeaveReportFragment().apply {}
        val tag: String = LeaveReportFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentLeaveReportBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        initClickLister()
        fetchEmployeeLeaveReport()
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

        dataAdapter.onItemClick = { model->
            goToLeaveDetails(model)
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

    private fun goToLeaveDetails(model: LeaveData) {
        val bundle = bundleOf(
            "model" to model
        )
        Timber.d("LeaveSessionDataTest 1 $bundle")
        findNavController().navigate(R.id.nav_leave_report_details, bundle)
    }

    private fun fetchEmployeeLeaveReport(){
        viewModel.fetchEmployeeLeaveReport().observe(viewLifecycleOwner, Observer { list->
            dataAdapter.initLoad(list.data!!)
            binding?.emptyView?.isVisible = list.data!!.isEmpty()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}