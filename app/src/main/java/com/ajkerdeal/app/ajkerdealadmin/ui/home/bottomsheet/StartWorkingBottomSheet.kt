package com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentStartWorkingBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.CustomSpinnerAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread

class StartWorkingBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentStartWorkingBottomSheetBinding? = null
    var onWorkingPlaceTypeClicked: ((workPlace: String) -> Unit)? = null

    private var selectedType = 0

    companion object {
        fun newInstance(): StartWorkingBottomSheet = StartWorkingBottomSheet().apply {}
        val tag: String = StartWorkingBottomSheet::class.java.name
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
        return FragmentStartWorkingBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()

        binding?.startedWorkingBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val workPlace = binding?.WorkingPlaceTypeTV?.text.toString().trim()
                onWorkingPlaceTypeClicked?.invoke(workPlace)
            }
        }
    }

    private fun validate(): Boolean {

        val breakReason = binding?.WorkingPlaceTypeTV?.text.toString()

        if (selectedType == 0) {
            context?.toast("Please, Select Working Place")
            return false
        }

        if (breakReason.trim().isEmpty()) {
            context?.toast("Please, Write Your Working Place")
            return false
        }

        return true
    }

    private fun setUpSpinner() {

        val pickupBreakReasonList: MutableList<String> = mutableListOf()
        pickupBreakReasonList.add("Select Working Place")
        pickupBreakReasonList.add("Office")
        pickupBreakReasonList.add("Field")
        pickupBreakReasonList.add("Home")
        pickupBreakReasonList.add("Other")

        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, pickupBreakReasonList)
        binding?.spinnerWorkingPlaceType?.adapter = spinnerAdapter
        binding?.spinnerWorkingPlaceType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedType = position
                if (position > 0) {
                    binding?.WorkingPlaceTypeTV?.setText(pickupBreakReasonList[position])
                    if (position == pickupBreakReasonList.lastIndex) {
                        binding?.WorkingPlaceTypeTV?.text?.clear()
                        binding?.WorkingPlaceTypeTV?.visibility = View.VISIBLE
                        binding?.WorkingPlaceTypeTV?.requestFocus()
                    } else {
                        binding?.WorkingPlaceTypeTV?.visibility = View.GONE
                    }
                } else {
                    binding?.WorkingPlaceTypeTV?.visibility = View.GONE
                }
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}