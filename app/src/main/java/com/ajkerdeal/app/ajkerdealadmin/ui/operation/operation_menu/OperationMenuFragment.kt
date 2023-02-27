package com.ajkerdeal.app.ajkerdealadmin.ui.operation.operation_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.bulk_status.StatusUpdateData
import com.ajkerdeal.app.ajkerdealadmin.api.models.operation_menu.OperationMenu
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentOperationMenuBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.operation.collection_bottom_sheet.CollectionBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import org.koin.android.ext.android.inject

class OperationMenuFragment: Fragment() {

    private var binding: FragmentOperationMenuBinding? = null
    private val viewModel: OperationMenuViewModel by inject()
    private val dataAdapter = OperationMenuAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding?.root ?: FragmentOperationMenuBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        generateMenu()
        initListener()
    }

    private fun init() {
        binding?.recyclerView?.let { recyclerView ->
            recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = dataAdapter
            }
        }
    }

    private fun generateMenu() {
        val menuList: MutableList<OperationMenu> = mutableListOf()
        menuList.add(OperationMenu(1, "Hub Transfer"))
        menuList.add(OperationMenu(2, "Manual Collection"))
        dataAdapter.initLoad(menuList)
    }

    private fun initListener() {
        dataAdapter.onItemClick = { model, position ->
            when(model.id) {
                1 -> {
                    findNavController().navigate(R.id.nav_operationMenu_operation)
                }
                2 -> {
                    showCollectionBottomSheet()
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
    }

    private fun showCollectionBottomSheet() {
        val tag: String = CollectionBottomSheet.tag
        val dialog: CollectionBottomSheet = CollectionBottomSheet.newInstance()
        dialog.show(childFragmentManager, tag)
        dialog.onOrderCollectClicked = { orderId ->
            dialog.dismiss()
            updateCollectionStatus(orderId)
        }
    }

    private fun updateCollectionStatus(orderId: String) {
        // 3 - Manually Collect From Merchant
        val requestBody: MutableList<StatusUpdateData> = mutableListOf()
        requestBody.add(
            StatusUpdateData(
                SessionManager.dtUserId,
                orderId,
                3,
                "adminapp",
                "",
                "Manually Collect From Merchant"
            )
        )

        viewModel.updateCollectionStatus(requestBody).observe(viewLifecycleOwner, Observer { flag->
            if (flag) {
                binding?.parent?.snackbar("Updated!")
            } else {
                binding?.parent?.snackbar("Something went wrong!")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}