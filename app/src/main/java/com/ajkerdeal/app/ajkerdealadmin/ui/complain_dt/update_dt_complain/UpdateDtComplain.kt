package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.update_dt_complain

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainUpdateRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentUpdateDtComplainBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.ComplainViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class UpdateDtComplain : Fragment() {

    private var binding: FragmentUpdateDtComplainBinding? = null

    private val viewModel: ComplainViewModel by inject()

    private var selectedComplainType = 0
    private var selectedComplain = ""
    private var isIssueSolved:String = ""
    private var isIssueSolvedSelected: Boolean = false

    companion object {
        fun newInstance(): UpdateDtComplain = UpdateDtComplain().apply {}
        val tag: String = UpdateDtComplain::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Update DT Complain"
        return FragmentUpdateDtComplainBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickLister()
        initData()
    }

    private fun initClickLister() {
        binding?.updateBtn?.setOnClickListener {
             updateComplain()
        }
        binding?.statusCheckedRadioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.checkIssueSolved -> {
                    isIssueSolved = "9"
                    isIssueSolvedSelected = true
                }
                R.id.checkIssuePending -> {
                    isIssueSolved = "0"
                    isIssueSolvedSelected = true
                }
            }
        }
    }

    private fun updateComplain(){
       if (verify()){
           val bookingCode = binding?.bookingCode?.text?.trim().toString()
           val commentTv = binding?.commentTv?.text?.trim().toString()

           val requestBody = ComplainUpdateRequest(bookingCode,commentTv,selectedComplain,isIssueSolved,SessionManager.dtUserId)
           Timber.d("test:$requestBody")
           viewModel.updateDtComplain(requestBody).observe(viewLifecycleOwner, Observer { response->
               if (response > 0){
                   context?.toast("সফল ভাবে আপডেট হয়েছে")
               }else{
                context?.toast("কোথাও কোনো সমস্যা হচ্ছে, আবার চেষ্টা করুন")
            }
           })

           viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
               when (state) {
                   is ViewState.ShowMessage -> {
                       requireContext().toast(state.message)
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
    }

    private fun verify(): Boolean{
        val complain = binding?.commentTv?.text.toString()
        if (selectedComplainType==0) {
            context?.toast("কমপ্লেইন টাইপ সিলেক্ট করুন")
            return false
        }
        if (!isIssueSolvedSelected) {
            context?.toast("স্ট্যাটাস টাইপ সিলেক্ট করুন")
            return false
        }
        if (selectedComplainType==12) {
            if (complain.trim().isEmpty()) {
                context?.toast("আপনার অভিযোগ / মতামত লিখুন")
                return false
            }
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {

        val answerBy = arguments?.getInt("answerBy") ?: 0
        binding?.answerBy?.setText(answerBy.toString())

        val orderId = arguments?.getInt("orderId") ?: 0
        binding?.bookingCode?.setText(orderId.toString())

        val assignedTo = arguments?.getString("assignedTo") ?: ""
        binding?.assignedTo?.setText(assignedTo)

        val fullName = arguments?.getString("fullName") ?: ""
        binding?.fullName?.setText(fullName)

        val comments = arguments?.getString("comments") ?: ""
        binding?.commentTv?.setText(comments)

        val complainType = arguments?.getString("complainType") ?: ""
        setUpSpinner(complainType)

        val status = arguments?.getString("status") ?: ""
        binding?.statusCheckedRadioGroup?.check( if(status == "Pending") R.id.checkIssuePending else R.id.checkIssueSolved)

    }

    private fun setUpSpinner(preselectedItem: String) {

        val pickupComplain: MutableList<String> = mutableListOf()
        pickupComplain.add("কমপ্লেইন টাইপ সিলেক্ট করুন")
        pickupComplain.add("কাস্টমার এখনো পার্সেল ডেলিভারি পায় নাই")
        pickupComplain.add("রিটার্ন পার্সেল এখনো বুঝে পাই নাই")
        pickupComplain.add("COD পেমেন্ট এখনো পাই নাই")
        pickupComplain.add("পার্সেল এখনো কালেকশন হয় নাই")
        pickupComplain.add("ভুল ডেলিভারী আপডেটেড বাই কুরিয়ার")
        pickupComplain.add("চালানের সমস্যা")
        pickupComplain.add("হারিয়ে গেছে")
        pickupComplain.add("প্রোডাক্ট হারানোর সম্ভবনা")
        pickupComplain.add("কুরিয়ার থেকে হারিয়েছে")
        pickupComplain.add("ডেলিভারী হয়েছে কিন্তু টাকা অ্যাড হয়নি")
        pickupComplain.add("পে রিকোয়েস্ট করেছি কিন্তু টাকা পাইনি")
        pickupComplain.add("অন্য কমপ্লেইন")

        val indexOfSelectedItem = pickupComplain.indexOf(preselectedItem)
        Timber.d("testDebug $indexOfSelectedItem, $preselectedItem")
        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupComplain)
        binding?.spinnerComplainType?.adapter = spinnerAdapter
        binding?.spinnerComplainType?.setSelection(indexOfSelectedItem)
        binding?.spinnerComplainType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (pickupComplain[position] == preselectedItem && position == 12){
                    binding?.commentsLayout?.visibility = View.VISIBLE
                }else if (pickupComplain[position] !== preselectedItem && position == 12){
                    binding?.commentsLayout?.visibility = View.VISIBLE
                    binding?.commentTv?.text?.clear()
                }else if (pickupComplain[position] != preselectedItem){
                    val comment = parent?.selectedItem
                    binding?.commentTv?.setText(comment.toString())
               }
                else{
                    binding?.commentsLayout?.visibility = View.GONE
                }
                selectedComplainType= position
                selectedComplain = pickupComplain[position]

               if (position > 0) {
                    if (position == pickupComplain.lastIndex) {
                        binding?.commentsLayout?.visibility = View.VISIBLE
                        //binding?.commentTv?.text?.clear()
                    }else{
                        binding?.commentsLayout?.visibility = View.GONE
                    }
                    spinnerAdapter.getItem(position)!!
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}