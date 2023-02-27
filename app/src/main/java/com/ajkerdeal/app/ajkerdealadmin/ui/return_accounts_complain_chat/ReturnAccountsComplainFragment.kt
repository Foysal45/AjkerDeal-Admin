package com.ajkerdeal.app.ajkerdealadmin.ui.return_accounts_complain_chat

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
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentReturnAccountsComplainBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatConfigure
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReturnAccountsComplainFragment : Fragment() {

    private lateinit var firebaseDatabase: FirebaseFirestore
    private lateinit var historyCollection: CollectionReference

    private var userList: MutableList<ADUsersModel> = mutableListOf()

    private var binding: FragmentReturnAccountsComplainBinding? = null

    private lateinit var returnAccountsComplainAdapter: ReturnAccountsComplainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentReturnAccountsComplainBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {

        fun newInstance() = ReturnAccountsComplainFragment().apply {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initData()
        initClickLister()

    }

    private fun init() {

        returnAccountsComplainAdapter = ReturnAccountsComplainAdapter()
        returnAccountsComplainAdapter.setHasStableIds(true)
        with(binding?.returnsAccountsComplainRV!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = returnAccountsComplainAdapter
        }
    }

    private fun initData() {
        getAllUser()
        returnAccountsComplainAdapter.getUnseenCount = {model, unseenCountTv, progressBar ->
            fetchUnseenCount(model, unseenCountTv, progressBar)
        }
    }

    private fun initClickLister() {
        returnAccountsComplainAdapter.onItemClicked = { model ->
            goToChatActivity(model)

        }

        binding?.swipeRefresh?.setOnRefreshListener {
            initData()
        }

    }

    private fun getAllUser() {
        binding?.progressBar?.visibility = View.VISIBLE
        userList.clear()
        calculateListData(SessionManager.dtUserId)
        returnAccountsComplainAdapter.initLoad(userList)
        binding?.returnsAccountsComplainRV?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
        binding?.swipeRefresh?.isRefreshing = false
    }

    private fun calculateListData(dtUserId: Int): MutableList<ADUsersModel>{
        when(dtUserId){
            256->{ userList.add(ADUsersModel(fullName = "Post Shipment Admin", userId = 906, mobile = "01927155755", adminType = 7))}
            11 -> {userList.add(ADUsersModel(fullName = "Accounts Team", userId = 1444, mobile = "01200000000", adminType = 8, personalEmail = "accountTeam@test.com"))}
            652 -> {userList.add(ADUsersModel(fullName = "Return Team", userId = 1443, mobile = "01300000000", adminType = 7, personalEmail = "returnteam@test.com"))}
            132 -> {userList.add(ADUsersModel(fullName = "Complain Admin", userId = 938, mobile = "01555555555", adminType = 6, personalEmail = "admin.ajkerdeal@gmail.com"))}
            else -> {
                userList.add(ADUsersModel(fullName = "Complain Admin", userId = 938, mobile = "01555555555", adminType = 6, personalEmail = "admin.ajkerdeal@gmail.com"))
                userList.add(ADUsersModel(fullName = "Return Team", userId = 1443, mobile = "01300000000", adminType = 7, personalEmail = "returnteam@test.com"))
                userList.add(ADUsersModel(fullName = "Accounts Team", userId = 1444, mobile = "01200000000", adminType = 8, personalEmail = "accountTeam@test.com"))
                userList.add(ADUsersModel(fullName = "Post Shipment Admin", userId = 906, mobile = "01927155755", adminType = 7))
            }
        }
        return  userList
    }

    private fun goToChatActivity(model: ADUsersModel) {

        val firebaseCredential = FirebaseCredential(
            firebaseWebApiKey = BuildConfig.FirebaseWebApiKey
        )
        if(model.userId == 906){
            val senderData = ChatUserData(
                model.userId.toString(), model.userName, model.mobile,
                imageUrl = "https://static.ajkerdeal.com/images/admin_users/dt/938.jpg",
                role = "bondhu",
                fcmToken = SessionManager.firebaseToken
            )
            val config = ChatConfigure(
                "dt-bondhu",
                senderData,
                firebaseCredential = firebaseCredential,
                userType = "admin"
            )
            config.config(requireContext())
        } else {
            val senderData = ChatUserData(
                model.userId.toString(), model.userName, model.mobile,
                imageUrl = "https://static.ajkerdeal.com/images/admin_users/dt/938.jpg",
                role = "retention",
                fcmToken = SessionManager.firebaseToken
            )
            val config = ChatConfigure(
                "dt-retention",
                senderData,
                firebaseCredential = firebaseCredential,
                userType = "admin"
            )
            config.config(requireContext())
        }

        // Complain Admin
        /*if (SessionManager.dtUserId == 938) {
            senderData.role = "complain"
            config.documentName = "dt-complain"
        }*/

    }

    @SuppressLint("SetTextI18n")
    private fun fetchUnseenCount(model: ADUsersModel, unseenCountTv: TextView, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        unseenCountTv.visibility = View.GONE
        var historyNode = ""
        var unseenCount = 0
        if (model.userId == 906){
            historyNode = "chat/dt-bondhu/history/bondhu/${model?.userId}"
        } else {
            historyNode = "chat/dt-retention/history/retention/${model?.userId}"
        }
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