package com.ajkerdeal.app.ajkerdealadmin.ui.post_shipment_chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUsersInfoRequest


import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentPostShipmentChatBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatConfigure
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeFragment
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.history.ChatHistoryFragment
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import org.koin.android.ext.android.inject

class PostShipmentChatFragment : Fragment() {
    var binding: FragmentPostShipmentChatBinding? = null
    private val viewModel: PostShipmentChatViewModel by inject()
    private var dataAdapter = PostShipmentChatAdapter()
    private val visibleThreshold = 5
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPostShipmentChatBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initClickListener()
    }

    private fun initClickListener() {
        binding?.searchBtn?.setOnClickListener {
            fetchUsersInfo(0)
        }

        binding?.closeBtn?.setOnClickListener {
            binding?.searchET?.setText("")
            fetchUsersInfo(0)
        }
        dataAdapter.onItemClicked = { model ->
            goToChatActivityRider(model)
        }
    }
    private fun goToChatActivityRider(model: CourierUser) {
        val firebaseCredential = FirebaseCredential(
            firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
        )
        val senderData = ChatUserData(
            "906", "Post Shipment Admin", "",
            imageUrl = "https://static.ajkerdeal.com/images/bondhuprofileimage/906/profileimage.jpg",
            role = "bondhu",
            fcmToken = SessionManager.firebaseToken
        )
        val receiverData = ChatUserData(
            model.courierUserId.toString(), model.companyName ?: "Company Name", model.mobile.toString(),
            imageUrl = "https://static.ajkerdeal.com/images/bondhuprofileimage/${model.courierUserId}/profileimage.jpg",
            role = "dt", fcmToken = model.firebaseToken!!
        )

        ChatConfigure(
            "dt-retention",
            senderData,
            firebaseCredential = firebaseCredential,
            receiver = receiverData,
            userType = "user"
        ).config(requireContext())
    }
    private fun initView() {
        fetchUsersInfo(0)
        binding?.recyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }

    private fun fetchUsersInfo(index: Int) {
        val searchKey = binding?.searchET?.text?.toString() ?: ""
        val requestBody = CourierUsersInfoRequest(count = 20, index = index, search = searchKey)
        viewModel.getCourierUsersInfo(requestBody, index)
    }

    private fun initData() {
        viewModel.viewState.observe(viewLifecycleOwner, { state ->
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
        viewModel.pagingState.observe(viewLifecycleOwner, { state ->
            isLoading = false
            if (state.isInitLoad) {
                dataAdapter.initLoad(state.dataList)
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
                        fetchUsersInfo(currentItemCount)
                    }

                }
            }
        })
    }

    private fun goToChatCompose(firebaseApp: FirebaseApp, bundle: Bundle) {
        val fragment = ChatComposeFragment.newInstance(firebaseApp, bundle)
        val tag = ChatHistoryFragment.tag
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.container, fragment, tag)
            commit()
        }
    }

}