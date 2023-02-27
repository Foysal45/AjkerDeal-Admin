package com.ajkerdeal.app.ajkerdealadmin.ui.operation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.barcode.BarcodeInfo
import com.ajkerdeal.app.ajkerdealadmin.api.models.bulk_status.StatusUpdateData
import com.ajkerdeal.app.ajkerdealadmin.api.models.hub.HubInfo
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentOperationBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewPopupHubStatusInputDtcodeBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.barcode.BarcodeScanningActivity
import com.ajkerdeal.app.ajkerdealadmin.ui.operation.collection_bottom_sheet.CollectionBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.lang.Exception

class OperationFragment : Fragment() {

    private var binding: FragmentOperationBinding? = null
    private val viewModel: OperationViewModel by inject()

    private val dataAdapter = BarcodeInfoAdapter()
    private val hubList: MutableList<HubInfo> = mutableListOf()
    private var fromHub: HubInfo? = null
    private var toHub: HubInfo? = null
    private var operationType: OperationType = OperationType.HUB_RECEIVE_SEND

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding?.root ?: FragmentOperationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        fetchHub()
        initListener()
    }

    private fun init() {
        binding?.recyclerView?.let { recyclerView ->
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                adapter = dataAdapter
            }
        }
    }

    private fun initListener() {
        dataAdapter.onDelete = { model, position ->
            dataAdapter.removeData(model, position)
            binding?.emptyView?.isVisible = dataAdapter.itemCount == 0
        }
        binding?.scanBtn?.setOnClickListener {
            if (isCheckPermission()) {
                scanBarcode()
            }
        }

        binding?.updateBtn?.setOnClickListener {
            if (validation()) {
                alert(
                    "Update Status",
                    "Do you really want to update status?",
                    true, "Yes, Update", "Cancel"
                ) {
                    if (it == AlertDialog.BUTTON_POSITIVE) {
                        updateStatus()
                    }
                }.show()
            }
        }
        binding?.spinnerHubFrom?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromHub = if (position > 0) {
                    hubList[position - 1]
                } else {
                    null
                }
            }
        }
        binding?.spinnerHubTo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                toHub = if (position > 0) {
                    hubList[position - 1]
                } else {
                    null
                }
            }
        }
        binding?.operationType?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.operationReceiveSend -> {
                    operationType = OperationType.HUB_RECEIVE_SEND
                }
                R.id.operationReceive -> {
                    operationType = OperationType.HUB_RECEIVE
                }
                R.id.operationSend -> {
                    operationType = OperationType.HUB_SEND
                }
            }
        }
        val progressDialog = progressDialog("Processing please wait")
        progressDialog.setCancelable(false)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ViewState.ShowMessage -> {
                    requireContext().toast(state.message)
                }
                is ViewState.KeyboardState -> {
                    hideKeyboard()
                }
                is ViewState.ProgressState -> {
                    when (state.type) {
                        0 -> binding?.progressBar?.isVisible = state.isShow
                        1 -> {
                            if (state.isShow) {
                                progressDialog.show()
                            } else {
                                progressDialog.dismiss()
                            }
                        }
                    }
                }
            }
        })
        binding?.dtCodeInput?.setOnClickListener {
            showCollectionBottomSheet()
        }
    }

    private fun validation(): Boolean {

        if (fromHub == null) {
            binding?.parent?.snackbar("Select from hub")
            return false
        }

        if (toHub == null) {
            binding?.parent?.snackbar("Select to hub")
            return false
        }

        if (dataAdapter.itemCount == 0) {
            binding?.parent?.snackbar("Add parcel first!")
            return false
        }

        return true
    }

    private fun fetchHub() {
        viewModel.fetchAllHub().observe(viewLifecycleOwner, Observer { list ->
            hubList.clear()
            hubList.addAll(list)

            val fromHubList: MutableList<String> = mutableListOf()
            val toHubList: MutableList<String> = mutableListOf()
            fromHubList.add("From Hub")
            toHubList.add("To Hub")
            list.forEach {
                fromHubList.add(it.name ?: "hub")
                toHubList.add(it.name ?: "hub")
            }

            val fromHubAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, fromHubList)
            binding?.spinnerHubFrom?.adapter = fromHubAdapter

            val toHubAdapter = CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, toHubList)
            binding?.spinnerHubTo?.adapter = toHubAdapter

            binding?.spinnerHubFrom?.postDelayed({
                if (isAdded) {
                    val preselectFrom = list.indexOfFirst { it.value == "kakrail-hub" }
                    val preselectTo = list.indexOfFirst { it.value == "sa-hub" }
                    if (preselectFrom != -1) {
                        binding?.spinnerHubFrom?.setSelection(preselectFrom + 1)
                    }
                    if (preselectTo != -1) {
                        binding?.spinnerHubTo?.setSelection(preselectTo + 1)
                    }
                }
            }, 300L)
        })
    }

    private fun updateStatus() {

        val receiveRequestBody: MutableList<StatusUpdateData> = mutableListOf()
        val sendRequestBody: MutableList<StatusUpdateData> = mutableListOf()
        when (operationType) {
            OperationType.HUB_RECEIVE_SEND -> {
                dataAdapter.dataList().forEach { model ->
                    generateReceiveRequest(model, receiveRequestBody)
                    generateSendRequest(model, sendRequestBody)
                }
            }
            OperationType.HUB_RECEIVE -> {
                dataAdapter.dataList().forEach { model ->
                    generateReceiveRequest(model, receiveRequestBody)
                }
            }
            OperationType.HUB_SEND -> {
                dataAdapter.dataList().forEach { model ->
                    generateSendRequest(model, sendRequestBody)
                }
            }
            OperationType.HUB_SEND -> {
                dataAdapter.dataList().forEach { model ->
                    generateSendRequest(model, sendRequestBody)
                }
            }
        }
        viewModel.updateBulkStatus(operationType, receiveRequestBody, sendRequestBody).observe(viewLifecycleOwner, Observer { flag ->
            if (flag) {
                binding?.parent?.snackbar("Updated!")
                dataAdapter.clearList()
                binding?.emptyView?.isVisible = dataAdapter.itemCount == 0
            } else {
                binding?.parent?.snackbar("Something went wrong!")
            }
        })
    }

    private fun generateReceiveRequest(model: BarcodeInfo, dataList: MutableList<StatusUpdateData>) {
        // 37 - Receive From Hub    Received From Hub-
        dataList.add(
            StatusUpdateData(
                SessionManager.dtUserId, model.data,
                37, "adminapp",
                fromHub?.value ?: "",
                "Received From Hub-${fromHub?.name}"
            )
        )
    }

    private fun showCollectionBottomSheet() {
        val tag: String = CollectionBottomSheet.tag
        val dialog: CollectionBottomSheet = CollectionBottomSheet.newInstance(true)
        dialog.show(childFragmentManager, tag)
        dialog.onProductRecievedClicked = { orderId ->
            scanResult(orderId)
            dialog.dismiss()
        }
    }

    private fun generateSendRequest(model: BarcodeInfo, dataList: MutableList<StatusUpdateData>) {
        // 36 - Send To Hub         Sent To Hub-
        dataList.add(
            StatusUpdateData(
                SessionManager.dtUserId, model.data,
                36, "adminapp",
                fromHub?.value ?: "",
                "Sent To Hub-${toHub?.name}"
            )
        )
    }

    private fun scanBarcode() {
        val intent = Intent(requireContext(), BarcodeScanningActivity::class.java).apply {
            putExtra("pattern", AppConstant.dtCodePattern)
        }
        barcodeScanningLauncher.launch(intent)
    }

    private fun scanResult(data: String) {
        val operation = dataAdapter.addUniqueData(BarcodeInfo(data))
        if (!operation) binding?.parent?.snackbar("Parcel code already exist!")
        binding?.emptyView?.isVisible = dataAdapter.itemCount == 0
    }

    private val barcodeScanningLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val scannedData = result.data?.getStringExtra("data") ?: return@registerForActivityResult
            scanResult(scannedData)
        }
    }

    private fun isCheckPermission(): Boolean {
        val permission1 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val permission2 = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return when {
            permission1 == PackageManager.PERMISSION_GRANTED /*&& permission2 == PackageManager.PERMISSION_GRANTED*/ -> {
                true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                false
            }
            else -> {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                false
            }
        }
    }

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        var isCameraGranted: Boolean = false
        var isStorageGranted: Boolean = false
        permissions.entries.forEach { permission ->
            if (permission.key == Manifest.permission.CAMERA) {
                isCameraGranted = permission.value
            }
            if (permission.key == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                isStorageGranted = permission.value
            }
        }
        if (isCameraGranted /*&& isStorageGranted*/) {
            scanBarcode()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}