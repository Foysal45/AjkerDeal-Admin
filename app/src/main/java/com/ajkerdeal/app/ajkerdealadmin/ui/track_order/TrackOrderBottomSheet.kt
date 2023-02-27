package com.ajkerdeal.app.ajkerdealadmin.ui.track_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentTrackOrderBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.callHelplineNumber
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class TrackOrderBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentTrackOrderBottomSheetBinding? = null
    private  var dataBottomSheetAdapter: OrderTrackingAdapter = OrderTrackingAdapter()
    private val viewModel: TrackOrderViewModel by inject()
    private var orderId: String? = ""

    companion object {
        fun newInstance(orderId: String): TrackOrderBottomSheet = TrackOrderBottomSheet().apply {
            this.orderId = orderId
        }
        val tag: String = TrackOrderBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentTrackOrderBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickLister()
        initData()
    }

    private fun initView() {
        binding?.recyclerview?.let { view ->
            with(view) {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataBottomSheetAdapter
            }
        }
        if (orderId!=null){
            orderId = orderId?.uppercase()
        }
        binding?.trackOrderTitleTv?.text = orderId
    }

    private fun initClickLister(){
        dataBottomSheetAdapter.callEdesh = {mobileNo->
            requireParentFragment().callHelplineNumber(mobileNo)
        }
        dataBottomSheetAdapter.callDC = {mobileNo->
            requireParentFragment().callHelplineNumber(mobileNo)
        }
    }

    private fun initData(){
        trackOrder(orderId)
    }

    private fun trackOrder(orderId: String?) {
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.trackOrder(orderId).observe(viewLifecycleOwner,{ list ->
            binding?.progressBar?.visibility = View.GONE
            if(!list.isNullOrEmpty()){
                dataBottomSheetAdapter.initLoad(list)
            } else{
                binding?.emptyView?.visibility = View.VISIBLE
            }
        })
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(true)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parentLayout?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}