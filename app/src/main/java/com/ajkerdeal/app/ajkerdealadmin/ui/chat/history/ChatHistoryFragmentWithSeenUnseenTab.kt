package com.ajkerdeal.app.ajkerdealadmin.ui.chat.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.ChatUserData
import com.ajkerdeal.app.ajkerdealadmin.api.models.chat.HistoryData
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatActivity
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentChatHistoryWithSeenUnseenTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class ChatHistoryFragmentWithSeenUnseenTab: Fragment() {

    private var binding: FragmentChatHistoryWithSeenUnseenTabBinding? = null

    private lateinit var firebaseApp: FirebaseApp
    private lateinit var firebaseDatabase: FirebaseFirestore
    private lateinit var historyCollection: CollectionReference
    private lateinit var userSenderCollection: CollectionReference
    private var realTimeListenerRegistration: ListenerRegistration? = null

    private var selectedTab = 0

    private var sender: ChatUserData? = null
    private var receiver: ChatUserData? = null
    private var documentName: String? = null
    private var firebaseStorageUrl: String? = null
    private var firebaseWebApiKey: String? = null
    private var role: String? = null
    private var isLoading: Boolean = false

    private lateinit var dataAdapter: ChatHistoryAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    companion object {
        fun newInstance(firebaseApp: FirebaseApp, bundle: Bundle) = ChatHistoryFragmentWithSeenUnseenTab().apply {
            this.firebaseApp = firebaseApp
            this.arguments = bundle
        }

        val tag: String = ChatHistoryFragmentWithSeenUnseenTab::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentChatHistoryWithSeenUnseenTabBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initChat()
        initTabLayout()
        fetchHistoryData()
        updateSenderData()
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            (it as ChatActivity).setToolbar("Chat History")
        }
    }

    private fun initData() {

        val bundle = arguments
        documentName = bundle?.getString("documentName")
        firebaseStorageUrl = bundle?.getString("firebaseStorageUrl")
        firebaseWebApiKey = bundle?.getString("firebaseWebApiKey")
        sender = bundle?.getParcelable("sender")
        receiver = bundle?.getParcelable("receiver")
        role = sender?.role ?: throw Exception("Invalid sender role")

    }

    private fun initView() {
        dataAdapter = ChatHistoryAdapter()
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerView?.run {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = dataAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
        dataAdapter.onItemClicked = { model ->
            goToChatCompose(model)
        }

        /*binding?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val currentItemCount = recyclerView.layoutManager?.itemCount ?: 0
                    val lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    //Log.d("onScrolled","${!isLoading} $currentItemCount <= ${firstVisibleItem + visibleThreshold}")
                    if (!isLoading && currentItemCount <=  lastVisibleItem +  5*//*&& currentItemCount > queryLimit - 1 && currentItemCount < totalCount*//*) {
                        isLoading = true
                        fetchMoreHistoryData(20)
                    }
                }
            }
        })*/
    }

    private fun initChat() {
        val historyNode = "chat/$documentName/history/$role/${sender?.id}"
        Timber.d("HistoryNode $historyNode")
        firebaseDatabase = Firebase.firestore //FirebaseFirestore.getInstance(firebaseApp)
        historyCollection = firebaseDatabase.collection(historyNode)

        val userSenderNode = "chat/$documentName/user-$role"
        userSenderCollection = firebaseDatabase.collection(userSenderNode)
    }

    private fun initTabLayout() {
        if (selectedTab == 0){
            binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(0))
        } else if (selectedTab == 1){
            binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(1))
        }
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when(tab?.position){

                    0 -> {
                        selectedTab = 0
                        fetchHistoryData()
                    }

                    1 -> {
                        selectedTab = 1
                        fetchHistoryData()
                    }

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun fetchHistoryData() {
        binding?.progressBar?.isVisible = true
        historyCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                binding?.progressBar?.isVisible = false
                if (documents.isEmpty) {
                    binding?.emptyView?.isVisible = true
                    return@addOnSuccessListener
                }
                binding?.emptyView?.isVisible = false
                val chatHistoryList = documents.toObjects(HistoryData::class.java)

                val unseenChatHistory = chatHistoryList.filter { chatData ->
                    chatData.seenStatus == 0
                }

                val seenChatHistory = chatHistoryList.filter { chatData ->
                    chatData.seenStatus == 1
                }

                if (selectedTab == 0){
                    dataAdapter.initLoad(unseenChatHistory)
                } else if(selectedTab == 1){
                    dataAdapter.initLoad(seenChatHistory)
                }

                binding?.emptyView?.isVisible = dataAdapter.itemCount == 0

                initRealTimeListener()

            }.addOnFailureListener {
                binding?.progressBar?.isVisible = false
            }
    }

    /*private fun fetchMoreHistoryData(limit: Long = 20) {
        //fetch init data
        binding?.progressBar?.isVisible = true
        val lastModel = dataAdapter.lastItem()
        historyCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .startAfter(lastModel.date)
            .limit(limit)
            .get()
            .addOnSuccessListener { documents ->
                isLoading = false
                binding?.progressBar?.isVisible = false
                if (documents.isEmpty) return@addOnSuccessListener
                val chatHistoryList = documents.toObjects(HistoryData::class.java)
                dataAdapter.pagingLoad(chatHistoryList)
                Log.d("chatDebug", "fetchMoreChatData chatHistoryList $chatHistoryList")
            }.addOnFailureListener {
                isLoading = false
                binding?.progressBar?.isVisible = false
            }
    }*/

    private fun initRealTimeListener() {
        //listen for real time change
        realTimeListenerRegistration = historyCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener(realTimeUpdateLister)
    }

    private val realTimeUpdateLister = EventListener<QuerySnapshot> { snapshot, error ->
        if (error != null) return@EventListener
        if (snapshot != null && !snapshot.isEmpty) {
            for (doc in snapshot.documentChanges) {
                if (doc.type == DocumentChange.Type.ADDED) {

                    val chatList = doc.document.toObject(HistoryData::class.java)

                    if(selectedTab == 0){

                        if (chatList.seenStatus == 0){
                            dataAdapter.addNewData(chatList)
                            smoothScrollToNewMsg()
                        }

                    }else{

                        if (chatList.seenStatus == 1){
                            dataAdapter.addNewData(chatList)
                            smoothScrollToNewMsg()
                        }

                    }


                } else if (doc.type == DocumentChange.Type.MODIFIED) {

                    val chatList = doc.document.toObject(HistoryData::class.java)

                    if (selectedTab == 0){

                        if (chatList.seenStatus == 0){
                            dataAdapter.addUniqueHistory(chatList)
                            smoothScrollToNewMsg()
                        }

                    } else{

                        if (chatList.seenStatus == 1){
                            dataAdapter.addUniqueHistory(chatList)
                            smoothScrollToNewMsg()
                        }

                    }

                }

            }
        }
    }

    private fun smoothScrollToNewMsg() {
        //val currentItemCount = dataAdapter.itemCount
        val visibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
        Log.d("debugSmoothScroll", "$visibleItemPosition")
        if (visibleItemPosition < 4) {
            binding?.recyclerView?.smoothScrollToPosition(0)
        }
    }

    private fun updateSenderData() {
        userSenderCollection
            .document("${sender?.id}")
            .set(sender!!)
    }

    private fun goToChatCompose(model: HistoryData) {
        Timber.d("historyData $model")
        val bundle = bundleOf(
            "documentName" to documentName,
            "firebaseStorageUrl" to firebaseStorageUrl,
            "firebaseWebApiKey" to firebaseWebApiKey,
            "sender" to sender,
            "receiver" to ChatUserData(
                model.receiverId,
                model.receiverName,
                role = model.receiverRole
            )
        )

        val fragment = ChatComposeFragment.newInstance(firebaseApp, bundle)
        val tag = ChatComposeFragment.tag
        activity?.supportFragmentManager?.beginTransaction()?.run {
            replace(R.id.container, fragment, tag)
            addToBackStack(tag)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}