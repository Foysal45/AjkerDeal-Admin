package com.ajkerdeal.app.ajkerdealadmin.ui.contact_info


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewContactInfoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ContactInfoAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userList: MutableList<ADUsersModel> = mutableListOf()

    var onItemClicked: ((model: ADUsersModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewContactInfoBinding = ItemViewContactInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ContactInfoAdapter.ViewModel) {

            val model = userList[position]
            val binding = holder.binding

            binding.userName.text = model.fullName
            binding.userMobile.text = model.mobile

            if(model.mobile.isNullOrEmpty()){
                binding.userMobile.visibility = View.GONE
                binding.callBtn.visibility = View.GONE
            } else {
                binding.userMobile.visibility = View.VISIBLE
                binding.callBtn.visibility = View.VISIBLE
            }

            binding.userPic.let { view ->

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

    inner class ViewModel(val binding: ItemViewContactInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.callBtn.setOnClickListener {
                onItemClicked?.invoke(userList[absoluteAdapterPosition])
            }
        }

    }

    fun initLoad(list: List<ADUsersModel>){
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }

}