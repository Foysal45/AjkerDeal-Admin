package com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant.telesales_status_update_bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TelesalesCourierUsersModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentTeleSalesUpdateBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey.LoanSurveryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey.LoanSurveyAdapter
import com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant.TeleSalesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

class TeleSalesStatusUpdateBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentTeleSalesUpdateBottomSheetBinding? = null

    private var dataAdapter: TelesalesStatusUpdateAdapter = TelesalesStatusUpdateAdapter()
    private var dataAdapter2: LoanSurveyAdapter = LoanSurveyAdapter()

    private val viewModel: TeleSalesViewModel by inject()
    private val viewModel2: LoanSurveryViewModel by inject()

    private var companyName: String? = ""
    private var courierUserID: Int = 0
    private var globalSelectedPosition: Int = -1
    private var courierUsersList = mutableListOf<TelesalesCourierUsersModel>()

    var onSuccessSubmission: (() -> Unit)? = null

    companion object {
        fun newInstance(companyName: String, courierUserID: Int): TeleSalesStatusUpdateBottomSheet = TeleSalesStatusUpdateBottomSheet().apply {
            this.companyName = companyName
            this.courierUserID = courierUserID
        }

        val tag: String = TeleSalesStatusUpdateBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentTeleSalesUpdateBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickLister()
        initData()
    }

    private fun initView() {
        binding?.telesalesTitleTv?.text = companyName

        binding?.recyclerViewStatus?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                adapter = dataAdapter
                itemAnimator = null
            }
        }
        binding?.recyclerViewOtherServices?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
                adapter = dataAdapter2
                itemAnimator = null
            }
        }
    }


    private fun initClickLister() {
        dataAdapter.onItemClicked = { _, position ->
            globalSelectedPosition = (position + 1)
            if (position == 1 || position == 2 || position == 4) {
                binding?.buttonLayout?.isVisible = true
                binding?.descriptiveText?.isVisible = true
                binding?.recyclerViewOtherServices?.isVisible = true
                viewModel2.fetchCourierList().observe(viewLifecycleOwner, { list ->
                    dataAdapter2.initLoad(list)
                })
            } else {
                binding?.buttonLayout?.isVisible = false
                binding?.descriptiveText?.isVisible = false
                binding?.recyclerViewOtherServices?.isVisible = false
                updateTelesalesStatusData(status = globalSelectedPosition)
                dialog?.dismiss()

            }
        }
        dataAdapter2.onItemClicked = { model, position ->
            dataAdapter2.multipleSelection(model, position)
        }
        binding?.okButton?.setOnClickListener {
            courierUsersList.clear()
            dataAdapter2.getSelectedItemModelList().forEach { data ->
                courierUsersList.add(
                    TelesalesCourierUsersModel(
                        courierId = data.courierId,
                        courierName = data.courierName,
                        courierUserId = courierUserID,
                        teleSaleCourierUsersId = 0,
                        teleSales = globalSelectedPosition
                    )
                )
            }
            updateTelesalesStatusData(status = globalSelectedPosition, courierUsersList = courierUsersList)
        }
        binding?.cancelBtn?.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun initData() {
        val telesalesOption: MutableList<String> = mutableListOf()
        telesalesOption.add("Recently Interested")
        telesalesOption.add("Not Interested") //1
        telesalesOption.add("Business Closed") // 2
        telesalesOption.add("Don't Pick")
        telesalesOption.add("Late Interested") //4

        dataAdapter.initLoad(telesalesOption)

    }

    private fun updateTelesalesStatusData(status: Int, courierUsersList: List<TelesalesCourierUsersModel> = listOf()) {
        viewModel.updateTelesalesStatus(
            TeleSalesUpdateRequest(teleSales = status, teleSalesCourierUsers = courierUsersList), courierUserID
        ).observe(viewLifecycleOwner, Observer { model ->
            if (model != null) {
                //success
                dataAdapter2.getSelectedItemModelList()
                onSuccessSubmission?.invoke()
                dismiss()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}