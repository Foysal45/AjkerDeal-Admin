package com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet.status_break_down

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.OrderCountResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentStatusBreakDownBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread

class StatusBreakDownBottomSheet(): BottomSheetDialogFragment() {

    private var binding: FragmentStatusBreakDownBinding? = null
    var onItemClicked: ((model: OrderCountResponse, position: Int) -> Unit)? = null
    var onMapClick: ((model: OrderCountResponse, position: Int) -> Unit)? = null

    private var dataList: MutableList<OrderCountResponse> = mutableListOf()
    private var title: String? = ""

    companion object {
        fun newInstance(title: String, dataList: MutableList<OrderCountResponse>): StatusBreakDownBottomSheet = StatusBreakDownBottomSheet().apply {
            this.title = title
            this.dataList = dataList
        }
        val tag: String = StatusBreakDownBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()

        val dialog: BottomSheetDialog? = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(true)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        //val metrics = resources.displayMetrics

        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parent?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            with(BottomSheetBehavior.from(bottomSheet)) {
                //state = BottomSheetBehavior.STATE_COLLAPSED
                skipCollapsed = false
                isHideable = false

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentStatusBreakDownBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.titleTV?.text = title

        val dataAdapter = ReturnAdapter()
        with(binding?.recyclerview!!) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dataAdapter
        }
        dataAdapter.initData(dataList)
        dataAdapter.onItemClick = { position, model ->
            onItemClicked?.invoke(model, position)
        }
        /*dataAdapter.onMapClick = { position, model ->
            onMapClick?.invoke(model, position)
        }*/
    }


}