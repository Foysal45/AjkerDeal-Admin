package com.ajkerdeal.app.ajkerdealadmin.ui.last_days_session

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLastDaysSessionEndBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*
import kotlin.concurrent.thread

class LastDaysSessionEndBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentLastDaysSessionEndBottomSheetBinding? = null
    var onEmergencyEndClicked: ((time: String, workReport: String) -> Unit)? = null


    private var selectedHour24: String = ""
    private var workReport: String = ""
    private var selectedTimeStamp: Long = 0L


    companion object {
        fun newInstance(report: String): LastDaysSessionEndBottomSheet = LastDaysSessionEndBottomSheet().apply {
            workReport = report
        }
        val tag: String = LastDaysSessionEndBottomSheet::class.java.name
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
        return FragmentLastDaysSessionEndBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.timePickerLayout?.setOnClickListener {
            timePicker()
        }
        binding?.submitBtn?.setOnClickListener {
            if (binding?.timeRangePicker?.text.isNullOrEmpty()){
                context?.toast("Please select work end time")
            }else{
                hideKeyboard()
                onEmergencyEndClicked?.invoke(binding?.timeRangePicker?.text.toString(), workReport)
            }
        }
    }

    private fun timePicker() {
        val calender = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            calender.apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            selectedTimeStamp = calender.timeInMillis
            selectedHour24 = "$hourOfDay:$minute"
            binding?.timeRangePicker?.text = selectedHour24
        }, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false)
            .show()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}