package com.ajkerdeal.app.ajkerdealadmin.ui.leave_report.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.LeaveData
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLeaveReportDetailsBinding

class LeaveReportDetailsFragment : Fragment() {
    private var binding: FragmentLeaveReportDetailsBinding? = null
    private var dataAdapter: LeaveReportDetailsAdapter = LeaveReportDetailsAdapter()
    private var model: LeaveData? = null

    companion object {
        fun newInstance(model: LeaveData): LeaveReportDetailsFragment = LeaveReportDetailsFragment().apply {
            this.model = model
        }
        val tag: String = LeaveReportDetailsFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentLeaveReportDetailsBinding.inflate(inflater, container, false).also {
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


    }

    private fun fetchEmployeeLeaveReport(){
        val bundle: Bundle? = arguments
        bundle?.let {
            this.model = it.getParcelable("model")
        }
        dataAdapter.initLoad(model?.session!!)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}