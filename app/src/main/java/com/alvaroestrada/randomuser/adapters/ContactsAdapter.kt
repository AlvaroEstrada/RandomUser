package com.alvaroestrada.randomuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvaroestrada.randomuser.callbacks.ContactDiffCallback
import com.alvaroestrada.randomuser.databinding.ItemContactBinding
import com.alvaroestrada.randomuser.databinding.ItemContactLoadingBinding
import com.alvaroestrada.randomuser.models.ContactView
import com.bumptech.glide.Glide

class ContactsAdapter(private val onClick:(ContactView) -> Unit) : ListAdapter<ContactView, RecyclerView.ViewHolder>(ContactDiffCallback()) {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_SHIMMER = 1
    var showShimmer = true

    override fun getItemViewType(position: Int): Int {
        return if (showShimmer) VIEW_TYPE_SHIMMER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ContactViewHolder(binding)
        } else {
            val shimmerBinding = ItemContactLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ShimmerViewHolder(shimmerBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactViewHolder && !showShimmer) {
            val contact = getItem(position)
            holder.binding.contactName.text = contact.fullName
            holder.binding.contactEmail.text = contact.email
            Glide.with(holder.itemView.context).load(contact.mediumPicUrl).circleCrop().into(holder.binding.contactImage)
            holder.itemView.setOnClickListener { onClick(contact) }
        }
    }

    fun getPositionOfContact(contact: ContactView): Int {
        return currentList.indexOf(contact)
    }

    override fun getItemCount(): Int {
        return if (showShimmer) SHIMMER_ITEM_NUMBER else super.getItemCount()
    }

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)
    class ShimmerViewHolder(val binding: ItemContactLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val SHIMMER_ITEM_NUMBER = 10
    }



}