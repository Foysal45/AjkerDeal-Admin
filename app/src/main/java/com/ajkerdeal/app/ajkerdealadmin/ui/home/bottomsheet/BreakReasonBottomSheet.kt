package com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentBreakReasonBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread

class BreakReasonBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentBreakReasonBottomSheetBinding? = null
    var onTakingBreakClicked: ((breakReason: String) -> Unit)? = null

    private var selectedType = 0

    companion object {
        fun newInstance(): BreakReasonBottomSheet = BreakReasonBottomSheet().apply {}
        val tag: String = BreakReasonBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        val dialog: BottomSheetDialog? = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(false)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parent?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            with(BottomSheetBehavior.from(bottomSheet)) {
                skipCollapsed = true
                isHideable = false
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBreakReasonBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()

        binding?.takingBreakBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val breakReason = binding?.breakReasonTV?.text.toString().trim()
                onTakingBreakClicked?.invoke(breakReason)
            }
        }
    }

    private fun validate(): Boolean {

        val breakReason = binding?.breakReasonTV?.text.toString()

        if (selectedType == 0) {
            context?.toast("Please, Select Break Reason")
            return false
        }

        if (breakReason.trim().isEmpty()) {
            context?.toast("Please, Write Your Break Reason")
            return false
        }

        return true
    }

    private fun setUpSpinner() {

        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        pickupBreakReasonList.add("Select Break Reason")
        pickupBreakReasonList.add("Lunch Break")
        pickupBreakReasonList.add("Prayer Break")
        pickupBreakReasonList.add("Washroom Break")
        pickupBreakReasonList.add("Tea Break")
        pickupBreakReasonList.add("Other")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBreakReasonList)
        binding?.spinnerBreakReasonType?.adapter = spinnerAdapter
        binding?.spinnerBreakReasonType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedType = position
                if (position > 0) {
                    binding?.breakReasonTV?.setText(pickupBreakReasonList[position])
                    if (position == pickupBreakReasonList.lastIndex) {
                        binding?.breakReasonTV?.text?.clear()
                        binding?.breakReasonTV?.visibility = View.VISIBLE
                        binding?.breakReasonTV?.requestFocus()
                    } else {
                        binding?.breakReasonTV?.visibility = View.GONE
                    }
                } else {
                    binding?.breakReasonTV?.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}