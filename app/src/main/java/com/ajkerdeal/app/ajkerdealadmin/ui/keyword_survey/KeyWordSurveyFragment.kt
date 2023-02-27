package com.ajkerdeal.app.ajkerdealadmin.ui.keyword_survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection.CategoryModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection.CategoryData
import com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection.SubCategory
import com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection.SubSubCategory
import com.ajkerdeal.app.ajkerdealadmin.api.models.key_word_survey.UserSearchKeyAddRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentKeyWordSurveyBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.keyword_survey.category_selection_bottom_sheet.CategorySelectionBottomSheet
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class KeyWordSurveyFragment : Fragment() {
    private var binding: FragmentKeyWordSurveyBinding? = null
    private val viewModel: KeyWordSurveyViewModel by inject()

    private val categoryList: MutableList<CategoryModel> = mutableListOf()
    private val subCategoryList: MutableList<SubCategory> = mutableListOf()
    private val subSubCategoryList: MutableList<SubSubCategory> = mutableListOf()
    private var selectedCategoryId: Int = 0
    private var selectedCategoryName: String = ""
    private var selectedSubCategoryId: Int = 0
    private var selectedSubCategoryName: String = ""
    private var selectedSubSubCategoryId: Int = 0
    private var selectedSubSubCategoryName: String = ""
    private var routingName: String = ""

    companion object {
        fun newInstance(): KeyWordSurveyFragment = KeyWordSurveyFragment().apply {}
        val tag: String = KeyWordSurveyFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentKeyWordSurveyBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initClickLister()
    }

    private fun initClickLister() {
        binding?.etCategory?.setOnClickListener {
            if (categoryList.isEmpty()) {
                fetchCategoryData(0, 1)
            } else {
                selectCategory(1)
            }
        }
        binding?.etSubCategory?.setOnClickListener {
            if (selectedCategoryId != 0) {
                fetchCategoryData(selectedCategoryId, 2)
            }
        }
        binding?.etSubSubCategory?.setOnClickListener {
            selectCategory(3)
        }
        binding?.submitBtn?.setOnClickListener {
            addUserSearchKey()
        }
    }

    private fun fetchCategoryData(id: Int, operation: Int) {

        viewModel.fetchAllCategories(id).observe(viewLifecycleOwner, Observer { categoryList ->
            when (operation) {
                //cat
                1 -> {
                    this.categoryList.clear()
                    this.categoryList.addAll(categoryList)
                    selectCategory(operation)
                }
                //subCat / subSubCat
                2 -> {
                    subCategoryList.clear()
                    subCategoryList.addAll(categoryList.first().subCategory)
                    selectCategory(operation)
                }
            }


        })
    }

    private fun addUserSearchKey(){
        if (!validation()){
            return
        }else{
            val possibleKeyWordEng = binding?.possibleKeyWordEng?.text?.trim().toString()
            val possibleKeyWordBengali = binding?.possibleKeyWordBan?.text?.trim().toString()

            val requestBody = UserSearchKeyAddRequest(selectedCategoryId, selectedSubCategoryId, selectedSubSubCategoryId,selectedCategoryName, selectedSubCategoryName, selectedSubSubCategoryName, routingName, possibleKeyWordEng, possibleKeyWordBengali)
            viewModel.addUserSearchKey(requestBody).observe(viewLifecycleOwner, Observer { response->
                if (response.status){
                    clearInput()
                    context?.toast("Added Successfully")
                }
            })
        }
    }

    private fun selectCategory(operation: Int) {
        val categoryDataList: MutableList<CategoryData> = mutableListOf()
        when (operation) {
            //cat
            1 -> {
                categoryList.forEach { model ->
                    categoryDataList.add(
                        CategoryData(
                            model.categoryId,
                            model.categoryName,
                            model.categoryNameInEnglish,
                            model.isSubCategoryAvailable,
                            model.categoryNameInEnglish?.lowercase(Locale.US) ?: "",
                            model.routingName ?: ""
                        )
                    )
                }
            }
            //subCat
            2 -> {
                subCategoryList.forEach { model ->
                    categoryDataList.add(
                        CategoryData(
                            model.subCategoryId,
                            model.subCategoryName,
                            model.subCategoryNameInEnglish,
                            model.categoryId,
                            model.subCategoryNameInEnglish?.lowercase(Locale.US) ?: "",
                            model.routingName ?: ""
                        )
                    )
                }
            }
            //subSubCat
            3 -> {
                subSubCategoryList.forEach { model ->
                    categoryDataList.add(
                        CategoryData(
                            model.subSubCategoryId,
                            model.subSubCategoryName,
                            model.subSubCategoryNameInEnglish,
                            model.categoryId,
                            model.subSubCategoryNameInEnglish?.lowercase(Locale.US) ?: "",
                            model.routingName ?: ""
                        )
                    )
                }
            }
        }
        goToCategorySelectDialogue(categoryDataList, operation)
    }

    private fun goToCategorySelectDialogue(list: MutableList<CategoryData>, operationFlag: Int) {

        val dialog = CategorySelectionBottomSheet.newInstance(list)
        dialog.show(childFragmentManager, CategorySelectionBottomSheet.tag)
        dialog.onCategoryPicked = { position, model ->
            when (operationFlag) {
                //cat
                1 -> {
                    selectedCategoryId = model.id
                    selectedCategoryName = model.displayNameEng!!
                    binding?.etCategory?.setText(model.displayNameBangla)
                    routingName = model.routingPath
                    selectedSubCategoryId = 0
                    selectedSubSubCategoryId = 0
                    binding?.etSubCategory?.setText("")
                    binding?.etSubSubCategory?.setText("")
                    binding?.etSubCategoryLayout?.isVisible = model.displayCategoryId == 1
                }
                //subCat
                2 -> {
                    selectedSubCategoryId = model.id
                    selectedSubCategoryName = model.displayNameEng!!
                    binding?.etSubCategory?.setText(model.displayNameBangla)
                    routingName = model.routingPath
                    selectedSubSubCategoryId = 0
                    binding?.etSubSubCategory?.setText("")
                    val subCatModel = subCategoryList[position]
                    subSubCategoryList.clear()
                    subSubCategoryList.addAll(subCatModel.subSubCategory)
                    binding?.etSubSubCategoryLayout?.isVisible = subSubCategoryList.isNotEmpty()
                }
                //subSubCat
                3 -> {
                    selectedSubSubCategoryId = model.id
                    selectedSubSubCategoryName = model.displayNameEng!!
                    binding?.etSubSubCategory?.setText(model.displayNameBangla)
                }
            }
        }

    }

    private fun clearInput(){
        selectedCategoryId = 0
        selectedSubCategoryId = 0
        selectedSubSubCategoryId = 0
        binding?.etCategory?.setText("")
        binding?.etSubCategory?.setText("")
        binding?.etSubSubCategory?.setText("")
        binding?.possibleKeyWordEng?.setText("")
        binding?.possibleKeyWordBan?.setText("")
    }

    private fun validation(): Boolean {

        val category = binding?.etCategory?.text?.trim().toString()
        val possibleKeyWordEng = binding?.possibleKeyWordEng?.text?.trim().toString()
        val possibleKeyWordBengali = binding?.possibleKeyWordBan?.text?.trim().toString()


        if (category.isEmpty()) {
            context?.toast("Please Select Category")
            return false
        }

        if (possibleKeyWordEng.isEmpty()) {
            context?.toast("Please Write English Possible Key Word")
            return false
        }

        if (possibleKeyWordBengali.isEmpty()) {
            context?.toast("সম্ভাব্য শব্দটি বাংলায় লিখুন")
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}