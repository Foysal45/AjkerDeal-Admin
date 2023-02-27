package com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesActiveMerchantDetailsResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentTeleSalesDetailsBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant.telesales_status_update_bottomsheet.TeleSalesStatusUpdateBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.callHelplineNumber
import org.koin.android.ext.android.inject

class TeleSalesDetailsFragment : Fragment() {
    private var binding: FragmentTeleSalesDetailsBinding? = null
    private val viewModel: TeleSalesViewModel by inject()

    private var dataAdapter: TeleSalesDetailsAdapter = TeleSalesDetailsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentTeleSalesDetailsBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initClickListener()
    }

    private fun initClickListener() {
        dataAdapter.onItemClicked = { model->
            telesalesUpdateStatusBottomSheetCall(model)
        }
        dataAdapter.onMobileNumberClick = { mobileNumber ->
           callHelplineNumber(mobileNumber.mobile)
       }
    }
     private fun telesalesUpdateStatusBottomSheetCall(modelTelesales: TeleSalesActiveMerchantDetailsResponse) {
            val tag = TeleSalesStatusUpdateBottomSheet.tag
            val dialog = TeleSalesStatusUpdateBottomSheet.newInstance(modelTelesales.companyName, modelTelesales.courierUserId)
            dialog.onSuccessSubmission = {
                //dataAdapter.clearData()
                //fetchTelesalesData(spinnerSelectedPosition, selectedDate)
            }
            dialog.show(childFragmentManager, tag)
        }
    private fun initView() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = arguments?.getString("title") ?: "Details"
        with(binding?.recyclerView!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
        }
    }

    private fun initData() {
        val status = arguments?.getInt("status") ?: 0
        val date = arguments?.getString("date") ?: ""
        viewModel.telesalesDetails(status, date).observe(viewLifecycleOwner, {
            dataAdapter.initLoad(it)
        })
    }
}