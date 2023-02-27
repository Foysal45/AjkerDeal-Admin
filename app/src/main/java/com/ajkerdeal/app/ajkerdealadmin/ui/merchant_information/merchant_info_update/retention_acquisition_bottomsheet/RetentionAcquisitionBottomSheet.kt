package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.retention_acquisition_bottomsheet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLocationSelectionDialogBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread

class RetentionAcquisitionBottomSheet : BottomSheetDialogFragment() {
    private var binding: FragmentLocationSelectionDialogBinding? = null
    private var dataList: List<ADUsersModel> = mutableListOf()

    private var handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    var onLocationPicked: ((model: ADUsersModel) -> Unit)? = null

    companion object {
        fun newInstance(dataList: List<ADUsersModel>): RetentionAcquisitionBottomSheet = RetentionAcquisitionBottomSheet().apply {
            this.dataList = dataList
        }

        val tag: String = RetentionAcquisitionBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme2)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentLocationSelectionDialogBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationAdapter = RetentionAcquisitionAdapter()
        locationAdapter.setDataList(dataList)
        with(binding?.placeListRv) {
            this?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            this?.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            this?.adapter = locationAdapter
        }
        locationAdapter.onItemClicked = { position, value ->
            hideKeyboard()
            onLocationPicked?.invoke(value)// position will change during search
            dismiss()
        }

//        manageSearch()

        binding?.crossBtn?.setOnClickListener {
            hideKeyboard()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(false)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        val metrics = resources.displayMetrics
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            thread {
                activity?.runOnUiThread {
                    //val dynamicHeight = parentLayout.height
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
        //extraSpace.minimumHeight = (metrics.heightPixels)
    }

    override fun onStop() {
        super.onStop()
        workRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}