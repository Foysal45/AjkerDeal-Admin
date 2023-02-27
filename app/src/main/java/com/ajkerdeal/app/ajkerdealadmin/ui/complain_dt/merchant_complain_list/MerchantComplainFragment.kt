package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMerchantComplainListBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.ViewState
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list.complain_history.MerchantComplainHistoryBottomSheet
import org.koin.android.ext.android.inject

class MerchantComplainFragment(): Fragment() {

    private var binding: FragmentMerchantComplainListBinding? = null
    private val viewModel: MerchantComplainViewModel by inject()

    private lateinit var dataAdapter: MerchantComplainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentMerchantComplainListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComplainList()
        fetchComplain()
        initClickLister()

        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
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

    private fun initComplainList() {

        dataAdapter = MerchantComplainAdapter()
        with(binding?.recyclerview!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
            //addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        dataAdapter.onItemClicked = {model,_->
            goToComplainHistoryBottomSheet(model.orderId)
        }
    }

    private fun initClickLister() {
        dataAdapter.onUpdateBtnClick = {model, position ->
            val bundle = bundleOf(
                "orderId" to model.orderId,
                "answerBy" to model.answerBy,
                "assignedTo" to model.assignedTo,
                "fullName" to model.fullName,
                "comments" to model.comments,
                "complainType" to model.complainType,
                "status" to model.currentStatus
            )
            findNavController().navigate(R.id.action_merchantComplainListFragment_to_updateMerchantComplain,bundle)
        }
    }

    private fun goToComplainHistoryBottomSheet(bookingCode: Int) {
        val tag = MerchantComplainHistoryBottomSheet.tag
        val dialog = MerchantComplainHistoryBottomSheet.newInstance(bookingCode)
        dialog.show(childFragmentManager, tag)
    }

    private fun fetchComplain() {

        val courierUSerId = arguments?.getInt("courierUserId", 0) ?: 0

        viewModel.fetchMerchantComplainList(courierUSerId , 0).observe(viewLifecycleOwner, Observer { list ->

            if (list.isNullOrEmpty()){
                binding?.emptyView?.visibility = View.VISIBLE
            }else{
                binding?.complaintTitle?.visibility = View.VISIBLE
                binding?.emptyView?.visibility = View.GONE
                dataAdapter.initLoad(list)
            }
        })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


}