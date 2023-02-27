package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.MerchantUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentMerchantInfoUpdateListBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import org.koin.android.ext.android.inject
import timber.log.Timber


class MerchantInformationFragmentList : Fragment() {

    private var binding: FragmentMerchantInfoUpdateListBinding? = null
    private val viewModel: MerchantInfoViewModelList by inject()

    private var dataAdapter: MerchantInformationAdapter = MerchantInformationAdapter()

    // Paging params
    private var isLoading = false
    private val visibleThreshold = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickLister()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentMerchantInfoUpdateListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    private fun initView() {

        binding?.recyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }

        getSrWiseCourierUsersInfo(0)

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

        viewModel.pagingState.observe(viewLifecycleOwner, Observer { state ->
            isLoading = false
            if (state.isInitLoad) {
                dataAdapter.initLoad(state.dataList)
                Timber.d("debugData ${state.dataList}")
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

        binding?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val currentItemCount = (recyclerView.layoutManager as LinearLayoutManager).itemCount
                    val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (!isLoading && currentItemCount <= lastVisibleItem + visibleThreshold) {
                        isLoading = true
                        getSrWiseCourierUsersInfo(currentItemCount)
                    }
                }
            }
        })

    }

    private fun initClickLister() {

        binding?.searchBtn?.setOnClickListener{
            getSrWiseCourierUsersInfo(0)
        }

        binding?.closeBtn?.setOnClickListener{
            binding?.searchET?.setText("")
            getSrWiseCourierUsersInfo(0)
        }

        dataAdapter.onItemClicked = { model, _ ->
            gotoMerchantUpdateProfileFragment(model)
        }
        dataAdapter.onMobileNumberClick = { mobileNumber ->
            callHelplineNumber(mobileNumber)
        }
    }

    private fun gotoMerchantUpdateProfileFragment(model: CourierUser) {
        val bundle = bundleOf("merchantInfoModel" to model)
        findNavController().navigate(R.id.action_merchantInformation_to_merchantInformationUpdateFragment, bundle)
    }

    private fun getSrWiseCourierUsersInfo(index: Int, count: Int = 20) {
        val searchKey = binding?.searchET?.text?.toString() ?: ""
        val requestBody = MerchantUpdateRequest(count, index, SessionManager.dtUserId, searchKey)
        viewModel.getSrWiseCourierUsersInfo(requestBody, index)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}