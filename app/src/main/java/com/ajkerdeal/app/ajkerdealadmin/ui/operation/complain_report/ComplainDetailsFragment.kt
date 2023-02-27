package com.ajkerdeal.app.ajkerdealadmin.ui.operation.complain_report

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainCommentUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainDetailsBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainHistoryBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainReportBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.ComplainViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryAdapter
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread
import androidx.appcompat.app.AppCompatActivity


class ComplainDetailsFragment : Fragment() {

    private var binding: FragmentComplainDetailsBinding? = null
    private val viewModel: ComplainViewModel by inject()
    private val dataAdapter: ComplainDetailsDataAdapter = ComplainDetailsDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentComplainDetailsBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {

        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Details (${arguments?.getString("fullName") ?: ""})")
        val typeFlag = arguments?.getInt("typeFlag") ?: 0
        val fromData = arguments?.getString("fromDate") ?: "2021-11-09"
        val toDate = arguments?.getString("toDate") ?: "2021-11-09"
        val assignedTo = arguments?.getInt("assignedTo") ?: 0
        val updatedBy = arguments?.getInt("updatedBy") ?: 0
        val isIssueSolved = arguments?.getInt("isIssueSolved") ?: 0
        val requestBody = AutoAssignedCountDetailsRequest(
            assignedTo = assignedTo,
            flag = typeFlag,
            fromDate = fromData,
            isIssueSolved = isIssueSolved,
            toDate = toDate,
            updatedBy = updatedBy
        )
        viewModel.assignedComplainCountDetails(requestBody).observe(viewLifecycleOwner, {
            dataAdapter.initLoad(it)
        })
    }

    private fun initView() {

        binding?.recyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }
}