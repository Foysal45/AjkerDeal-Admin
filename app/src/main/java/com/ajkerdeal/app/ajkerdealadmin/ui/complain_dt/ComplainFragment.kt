package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentComplainBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.ChatActivity
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager.userFullName
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class ComplainFragment(): Fragment() {

    private var binding: FragmentComplainBinding? = null
    private val viewModel: ComplainViewModel by inject()

    private var selectedType = 0

    private var orderId: String? = ""
    private var title: String? = ""

    companion object {
        fun newInstance(orderId: String? = "", title: String? = ""): ComplainFragment = ComplainFragment().apply {
            this.orderId = orderId
            this.title = title
        }

        val tag: String = ComplainFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentComplainBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()
        if(!title.isNullOrEmpty()){
            activity?.let {
                (it as ChatActivity).setToolbar(title!!)
            }
        }
        if(!orderId.isNullOrEmpty()){
            binding?.orderCodeTV?.setText(orderId?.uppercase())
        }

        binding?.submitBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {

                val orderCode = binding?.orderCodeTV?.text.toString().trim()
                val complain = binding?.complainTV?.text.toString().trim()
                val code = orderCode.uppercase(Locale.US).replace("DT-", "")

                val requestBody = ComplainRequest(orderCode, complain,"adadminapp", SessionManager.userFullName, SessionManager.mobile)
                viewModel.submitComplain(requestBody).observe(viewLifecycleOwner, Observer { complainStatus->
                    when {
                        complainStatus > 0 -> {
                            binding?.orderCodeTV?.text?.clear()
                            binding?.complainTV?.text?.clear()
                            binding?.spinnerComplainType?.setSelection(0)

                            context?.toast("আপনার অভিযোগ / মতামত সাবমিট হয়েছে")
                        }
                        complainStatus == -1 -> {
                            context?.toast("এই কমপ্লেইন ইতিমধ্যে করা হয়েছে")
                        }
                        else -> {
                            context?.toast("কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন")
                        }
                    }
                })
            }
        }

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

    private fun validate(): Boolean {

        val orderCode = binding?.orderCodeTV?.text.toString()
        val complain = binding?.complainTV?.text.toString()

        if (selectedType == 1 || selectedType == 2) {
            if (orderCode.trim().isEmpty()) {
                context?.toast("অর্ডার আইডি লিখুন")
                return false
            }
        }

        if (orderCode.trim().isNotEmpty() && !isValidDTCode(orderCode)) {
            context?.toast("সঠিক অর্ডার আইডি লিখুন")
            return false
        }

        if (selectedType == 0) {
            context?.toast("কমপ্লেইন টাইপ সিলেক্ট করুন")
            return false
        }

        if (selectedType == 13) {
            if (complain.trim().isEmpty()) {
                context?.toast("আপনার অভিযোগ / মতামত লিখুন")
                return false
            }
        }

        return true
    }

    private fun setUpSpinner() {

        val pickupDistrictList: MutableList<String> = mutableListOf()
        pickupDistrictList.add("কমপ্লেইন টাইপ সিলেক্ট করুন")
        pickupDistrictList.add("কাস্টমার এখনো পার্সেল ডেলিভারি পায় নাই")
        pickupDistrictList.add("রিটার্ন পার্সেল এখনো বুঝে পাই নাই")
        pickupDistrictList.add("COD পেমেন্ট এখনো পাই নাই")
        pickupDistrictList.add("পার্সেল এখনো কালেকশন হয় নাই")
        pickupDistrictList.add("ভুল ডেলিভারী আপডেটেড বাই কুরিয়ার")
        pickupDistrictList.add("চালানের সমস্যা")
        pickupDistrictList.add("হারিয়ে গেছে")
        pickupDistrictList.add("প্রোডাক্ট হারানোর সম্ভবনা")
        pickupDistrictList.add("কুরিয়ার থেকে হারিয়েছে")
        pickupDistrictList.add("ডেলিভারী হয়েছে কিন্তু টাকা অ্যাড হয়নি")
        pickupDistrictList.add("পে রিকোয়েস্ট করেছি কিন্তু টাকা পাইনি")
        pickupDistrictList.add("অন্য কমপ্লেইন")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupDistrictList)
        binding?.spinnerComplainType?.adapter = spinnerAdapter
        binding?.spinnerComplainType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedType = position
                binding?.orderCodeTV?.visibility = View.GONE
                if (position > 0) {
                    binding?.complainTV?.setText(pickupDistrictList[position])
                    binding?.orderCodeTV?.visibility = View.VISIBLE
                    if (position == pickupDistrictList.lastIndex) {
                        binding?.complainTV?.text?.clear()
                        binding?.complainTV?.visibility = View.VISIBLE
                    }else{
                        binding?.complainTV?.visibility = View.GONE
                    }
                } else {
                    binding?.complainTV?.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


}
