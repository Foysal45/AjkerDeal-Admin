package com.ajkerdeal.app.ajkerdealadmin.ui.keyword_survey.category_selection_bottom_sheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.category_selection.CategoryData


class CategorySelectionAdapter(private var dataList: MutableList<CategoryData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClicked: ((position: Int, value: CategoryData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_select_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolder) {
            val model = dataList[position]
            holder.displayNameBangla.text = model.displayNameBangla
            holder.displayNameEnglish.text = model.displayNameEng
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        internal val displayNameBangla: TextView = view.findViewById(R.id.categoryName)
        internal val displayNameEnglish: TextView = view.findViewById(R.id.categoryNameEng)

        init {
            view.setOnClickListener {
                val index = adapterPosition
                if (index >= 0 && index < dataList.size) {
                    onItemClicked?.invoke(index, dataList[index])
                }
            }
        }
    }

    fun setDataList(list: List<CategoryData>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
}