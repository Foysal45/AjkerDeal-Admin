package com.ajkerdeal.app.ajkerdealadmin.ui.return_accounts_complain_chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewRetentionUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ReturnAccountsComplainAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //private lateinit var firebaseDatabase: FirebaseFirestore
    //private lateinit var historyCollection: CollectionReference

    private var userList: MutableList<ADUsersModel> = mutableListOf()

    var onItemClicked: ((model: ADUsersModel) -> Unit)? = null

    var getUnseenCount: ((model: ADUsersModel, unseenCountTv: TextView, progressBar: ProgressBar) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewRetentionUserBinding = ItemViewRetentionUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ReturnAccountsComplainAdapter.ViewModel) {

            val model = userList[position]
            val binding = holder.binding

            binding.retentionUserName.text = model.fullName
            binding.retentionUserMobile.text = model.mobile
            getUnseenCount?.invoke(model, binding.unseenCount, binding.progressBar)

            binding.retentionUserPic.let { view ->

                val myOptions = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_person_circle).error(R.drawable.ic_person_circle).circleCrop()

                Glide.with(view)
                    .asBitmap()
                    .apply(myOptions)
                    .load("https://static.ajkerdeal.com/images/admin_users/dt/${model.userId}.jpg")
                    .into(view)

            }

        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewModel(val binding: ItemViewRetentionUserBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked?.invoke(userList[absoluteAdapterPosition])
                //getUnseenCount?.invoke(userList[absoluteAdapterPosition], binding.unseenCount)
            }
        }

    }

    fun initLoad(list: List<ADUsersModel>){
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}