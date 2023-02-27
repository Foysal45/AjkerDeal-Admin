package com.ajkerdeal.app.ajkerdealadmin.ui.retention_users_chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.HistoryData
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentRetentionUsersListBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatConfigure
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject

class RetentionUsersListFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseFirestore
    private lateinit var historyCollection: CollectionReference

    private var binding: FragmentRetentionUsersListBinding? = null
    private val viewModel: RetentionUsersListViewModel by inject()

    private lateinit var retentionUserAdapter: RetentionUsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRetentionUsersListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {

        fun newInstance() = RetentionUsersListFragment().apply {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initData()
        initClickLister()

    }

    private fun init() {

        retentionUserAdapter =RetentionUsersAdapter()
        retentionUserAdapter.setHasStableIds(true)
        with(binding?.retentionUsersRv!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = retentionUserAdapter
        }
    }

    private fun initData() {
        getAllUser()
        retentionUserAdapter.getUnseenCount = {model, unseenCountTv, progressBar ->
            fetchUnseenCount(model, unseenCountTv, progressBar)
        }
    }

    private fun initClickLister() {
        retentionUserAdapter.onItemClicked = { model ->
            goToChatActivity(model)

        }

        binding?.swipeRefresh?.setOnRefreshListener {
            initData()
        }

    }

    private fun getAllUser() {
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.getUserInfo().observe(viewLifecycleOwner, { allUsers ->
            binding?.progressBar?.visibility = View.GONE
            binding?.swipeRefresh?.isRefreshing = false
            if (!allUsers.isNullOrEmpty()){

                val filteredRetention = allUsers.filter { it.isRetention }

                binding?.progressBar?.visibility = View.GONE
                retentionUserAdapter.initLoad(filteredRetention)
                binding?.retentionUsersRv?.visibility = View.VISIBLE
            }
        })
    }

    private fun goToChatActivity(model: ADUsersModel) {

        val firebaseCredential = FirebaseCredential(
            firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
        )
        val senderData = ChatUserData(
            model.userId.toString(), model.userName, model.mobile,
            imageUrl = "https://static.ajkerdeal.com/images/admin_users/dt/${model.userId}.jpg",
            role = "retention",
            fcmToken = SessionManager.firebaseToken
        )
        val config = ChatConfigure(
            "dt-retention",
            senderData,
            firebaseCredential = firebaseCredential,
            userType = "admin"
        )
        // Complain Admin
        /*if (SessionManager.dtUserId == 938) {
            senderData.role = "complain"
            config.documentName = "dt-complain"
        }*/
        config.config(requireContext())

    }

    @SuppressLint("SetTextI18n")
    private fun fetchUnseenCount(model: ADUsersModel, unseenCountTv: TextView, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        unseenCountTv.visibility = View.GONE
        var unseenCount = 0
        val historyNode = "chat/dt-retention/history/retention/${model?.userId}"
        firebaseDatabase = Firebase.firestore //FirebaseFirestore.getInstance(firebaseApp)
        historyCollection = firebaseDatabase.collection(historyNode)
        historyCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    return@addOnSuccessListener
                }
                val chatHistoryList = documents.toObjects(HistoryData::class.java)

                chatHistoryList.forEach { data ->
                    if (data.seenStatus == 0){
                        unseenCount++
                    }
                }
                progressBar.visibility = View.GONE
                unseenCountTv.visibility = View.VISIBLE
                if(unseenCount != 0){
                    unseenCountTv.text = "($unseenCount)"
                    unseenCount = 0
                }

            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                unseenCountTv.visibility = View.VISIBLE

            }.addOnCompleteListener {
                progressBar.visibility = View.GONE
                unseenCountTv.visibility = View.VISIBLE
            }

    }

}