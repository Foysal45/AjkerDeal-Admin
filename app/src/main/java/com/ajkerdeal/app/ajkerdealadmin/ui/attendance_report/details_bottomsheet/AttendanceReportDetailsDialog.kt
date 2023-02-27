package com.ajkerdeal.app.ajkerdealadmin.ui.attendance_report.details_bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.report.WorkReportData
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentReportDetailsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread

class AttendanceReportDetailsDialog : BottomSheetDialogFragment() {

    private var binding: FragmentReportDetailsBottomSheetBinding? = null
    private  var dataAdapter: ReportDetailsBottomSheetAdapter = ReportDetailsBottomSheetAdapter()
    private var model: WorkReportData = WorkReportData()

    companion object {

        fun newInstance(data: WorkReportData): AttendanceReportDetailsDialog = AttendanceReportDetailsDialog().apply {
            model = data
        }
        val tag: String = AttendanceReportDetailsDialog::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentReportDetailsBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initDtaModel()

        binding?.employeeName?.text = if (model.userName.isNullOrEmpty()){
            "Employee"
        }else{
            model.userName
        }
        binding?.employeeId?.text = "(${model.userid})"
        binding?.workPlace?.text = if (model.from.isNullOrEmpty()) "-" else model.from
        binding?.workHour?.text = model.workingTime
        binding?.breakHour?.text = model.breakTime
        binding?.reportDate?.text = "Date: ${model.date}"

    }

    private fun initView() {
        binding?.recyclerView?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }

    private fun initDtaModel() {
        dataAdapter.initLoad(model.session!!)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(true)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        val metrics = resources.displayMetrics
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            thread {
                activity?.runOnUiThread {
                    BottomSheetBehavior.from(bottomSheet).peekHeight = metrics.heightPixels
                }
            }
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = false
            BottomSheetBehavior.from(bottomSheet).addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                }
                override fun onStateChanged(p0: View, p1: Int) {
                    /*if (p1 == BottomSheetBehavior.STATE_DRAGGING) {
                        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                    }*/
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
