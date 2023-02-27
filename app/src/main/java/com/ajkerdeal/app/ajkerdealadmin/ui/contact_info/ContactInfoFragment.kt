package com.ajkerdeal.app.ajkerdealadmin.ui.contact_info

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.AdUserReqBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentContactInfoBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import org.koin.android.ext.android.inject
import java.util.*

class ContactInfoFragment : Fragment() {

    private var binding: FragmentContactInfoBinding? = null
    private val viewModel: ContactInfoViewModel by inject()

    private var dataList: MutableList<ADUsersModel> = mutableListOf()
    private var dataListCopy: MutableList<ADUsersModel> = mutableListOf()

    private var selectedFilter = 0
    private var handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    private lateinit var contactInfoAdapter: ContactInfoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentContactInfoBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {

        fun newInstance() = ContactInfoFragment().apply {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initData()
        initClickLister()

    }

    private fun init() {

        setUpSpinner()

        contactInfoAdapter = ContactInfoAdapter()
        with(binding?.usersRv!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactInfoAdapter
        }

    }

    private fun initData() {
        getAllUser()
    }

    private fun initClickLister() {
        contactInfoAdapter.onItemClicked = { model ->
            callUser(model)

        }

        binding?.swipeRefresh?.setOnRefreshListener {
            initData()
        }

        manageSearch()

    }

    private fun manageSearch() {

        binding?.searchET?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    val searchKey = p0.toString().trim()
                    search(searchKey)
                }
                handler.postDelayed(workRunnable!!, 100L)
            }
        })

        binding?.searchBtn?.setOnClickListener {
            hideKeyboard()
            val searchKey = binding?.searchET?.text.toString()
            search(searchKey)
        }

        binding?.closeBtn?.setOnClickListener {
            binding?.searchET?.text?.clear()
        }
    }

    private fun search(searchKey: String) {

        binding?.progressBar?.visibility = View.VISIBLE
        if (dataListCopy.isEmpty()) {
            dataListCopy.clear()
            dataListCopy.addAll(dataList)
        }
        if (searchKey.isEmpty()) {
            (binding?.usersRv?.adapter as ContactInfoAdapter).initLoad(dataListCopy)
            binding?.progressBar?.visibility = View.GONE
            return
        }
        val filteredList = dataListCopy.filter { model->
            (model.fullName.contains(searchKey, true))
        }
        (binding?.usersRv?.adapter as ContactInfoAdapter).initLoad(filteredList)
        binding?.progressBar?.visibility = View.GONE
    }


    private fun getAllUser() {
        binding?.progressBar?.visibility = View.VISIBLE
        val reqBody = AdUserReqBody(SessionManager.adminType, "")
        viewModel.getUserInfo(reqBody).observe(viewLifecycleOwner, { allUsers ->
            binding?.progressBar?.visibility = View.GONE
            binding?.swipeRefresh?.isRefreshing = false
            if (!allUsers.isNullOrEmpty()){
                binding?.progressBar?.visibility = View.GONE
                when (selectedFilter) {
                    0 -> {
                        val filteredList = allUsers.filter {
                            it.isActive == 1
                        }
                        contactInfoAdapter.initLoad(filteredList)
                        dataAdd(filteredList)
                    }
                    1 -> {

                        val filteredList = allUsers.filter {
                            it.adminType == 0 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    2 -> {

                        val filteredList = allUsers.filter {
                            it.adminType == 1 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    3 -> {

                        val filteredList = allUsers.filter {
                            it.adminType == 2 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    4 -> {

                        val filteredList = allUsers.filter {
                            it.adminType == 4 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    5-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 6 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    6-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 7 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    7-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 8 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    8-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 9 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    9-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 10 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    10-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 11 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    11-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 12 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                    12-> {

                        val filteredList = allUsers.filter {
                            it.adminType == 13 && it.isActive == 1
                        }
                        dataAdd(filteredList)
                        contactInfoAdapter.initLoad(filteredList)

                    }
                }
                binding?.usersRv?.visibility = View.VISIBLE
            }
        })
    }

    private fun dataAdd(data: List<ADUsersModel>){
        dataList.clear()
        dataList.addAll(data)
    }

    private fun callUser(model: ADUsersModel) {
        if (!model.mobile.isNullOrEmpty()){
            callHelplineNumber(model.mobile)
        } else{
            context?.toast("Phone Number Not Found")
        }
    }

    private fun setUpSpinner() {

        val filterList: MutableList<String> = mutableListOf()
        filterList.add("All")
        filterList.add("Emerging")
        filterList.add("VIP")
        filterList.add("Manager")
        filterList.add("Sales")
        filterList.add("CRM")
        filterList.add("Fulfillment")
        filterList.add("Accounts")
        filterList.add("Social")
        filterList.add("Content")
        filterList.add("IT")
        filterList.add("Merchandising")
        filterList.add("Service Quality Analyst")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, filterList)
        binding?.filterSpinner?.adapter = spinnerAdapter
        binding?.filterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFilter = position
                getAllUser()
                binding?.usersRv?.scrollToPosition(0)
            }
        }

    }

}