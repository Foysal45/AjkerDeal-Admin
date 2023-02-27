package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.location_selection_bottom_sheet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.DistrictModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLocationSelectionDialogBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*
import kotlin.concurrent.thread

class LocationSelectionBottomSheet : BottomSheetDialogFragment() {
    private var binding: FragmentLocationSelectionDialogBinding? = null
    private var dataList: MutableList<DistrictModel> = mutableListOf()
    private var dataListCopy: MutableList<DistrictModel> = mutableListOf()

    private var handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null

    var onLocationPicked: ((model: DistrictModel) -> Unit)? = null

    companion object {
        fun newInstance(dataList: MutableList<DistrictModel>): LocationSelectionBottomSheet = LocationSelectionBottomSheet().apply {
            this.dataList = dataList
        }

        val tag: String = LocationSelectionBottomSheet::class.java.name
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

        val locationAdapter = LocationSelectionAdapter()
        locationAdapter.setDataList(dataList)
        with(binding?.placeListRv) {
            this?.layoutManager = LinearLayoutManager(requireContext(), androidx.recyclerview.widget.RecyclerView.VERTICAL, false)
            this?.addItemDecoration(DividerItemDecoration(requireContext(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
            this?.adapter = locationAdapter
        }
        locationAdapter.onItemClicked = { position, value ->
            hideKeyboard()
            onLocationPicked?.invoke(value)// position will change during search
            dismiss()
        }

        manageSearch()

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

    private fun manageSearch() {

        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                workRunnable?.let { handler.removeCallbacks(it) }
                workRunnable = Runnable {
                    val searchKey = p0.toString()
                    search(searchKey)
                }
                handler.postDelayed(workRunnable!!, 500L)
            }
        })

        binding?.searchBtn?.setOnClickListener {
            hideKeyboard()
            val searchKey = binding?.searchEditText?.text.toString()
            search(searchKey)
        }
    }

    private fun search(searchKey: String) {

        binding?.progressBar?.visibility = View.VISIBLE
        if (dataListCopy.isEmpty()) {
            dataListCopy.clear()
            dataListCopy.addAll(dataList)
        }
        if (searchKey.isEmpty()) {
            (binding?.placeListRv?.adapter as LocationSelectionAdapter).setDataList(dataListCopy)
            binding?.progressBar?.visibility = View.GONE
            return
        }
        val lowerCaseSearchKey = searchKey.toLowerCase(Locale.US)
        val filteredList = dataListCopy.filter { model ->
            (model.district.contains(searchKey))
        }
        (binding?.placeListRv?.adapter as LocationSelectionAdapter).setDataList(filteredList)
        binding?.progressBar?.visibility = View.GONE
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