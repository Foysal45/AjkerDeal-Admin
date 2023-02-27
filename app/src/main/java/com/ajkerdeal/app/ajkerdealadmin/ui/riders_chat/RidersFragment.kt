package com.ajkerdeal.app.ajkerdealadmin.ui.riders_chat

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
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.FirebaseCredential
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.HistoryData
import com.ajkerdeal.app.ajkerdealadmin.api.models.riders.RiderModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentRidersBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatConfigure
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.inject

class RidersFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseFirestore
    private lateinit var historyCollection: CollectionReference

    //todo make another layout
    private var binding: FragmentRidersBinding? = null
    private val viewModel: RidersViewModel by inject()

    private lateinit var ridersAdapter: RidersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentRidersBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {

        fun newInstance() = RidersFragment().apply {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initData()
        initClickLister()

    }

    private fun init() {

        ridersAdapter = RidersAdapter()
        ridersAdapter.setHasStableIds(true)
        with(binding?.ridersRv!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ridersAdapter
        }
    }

    private fun initData() {
        getAllUser()
        ridersAdapter.getUnseenCount = {model, unseenCountTv, progressBar ->
            fetchUnseenCount(model, unseenCountTv, progressBar)
        }
    }

    private fun initClickLister() {
        ridersAdapter.onItemClicked = { model ->
            goToChatActivity(model)

        }

        binding?.swipeRefresh?.setOnRefreshListener {
            initData()
        }

    }

    private fun getAllUser() {
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.getRidersInfo().observe(viewLifecycleOwner, { allRiders ->
            binding?.progressBar?.visibility = View.GONE
            binding?.swipeRefresh?.isRefreshing = false
            if (!allRiders.isNullOrEmpty()){

                binding?.progressBar?.visibility = View.GONE
                ridersAdapter.initLoad(allRiders)
                binding?.ridersRv?.visibility = View.VISIBLE
            }
        })
    }

    private fun goToChatActivity(model: RiderModel) {

        val firebaseCredential = FirebaseCredential(
            firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
        )
        val senderData = ChatUserData(
            model.id.toString(), model.name, model.mobile,
            imageUrl = "https://static.ajkerdeal.com/images/bondhuprofileimage/${model.id}/profileimage.jpg",
            role = "bondhu",
            fcmToken = SessionManager.firebaseToken
        )
        val config = ChatConfigure(
            "dt-bondhu",
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
    private fun fetchUnseenCount(model: RiderModel, unseenCountTv: TextView, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        unseenCountTv.visibility = View.GONE
        var unseenCount = 0
        val historyNode = "chat/dt-bondhu/history/bondhu/${model?.id}"
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