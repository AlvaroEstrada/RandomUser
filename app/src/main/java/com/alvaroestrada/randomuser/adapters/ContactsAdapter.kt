package com.alvaroestrada.randomuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alvaroestrada.randomuser.callbacks.ContactDiffCallback
import com.alvaroestrada.randomuser.databinding.ItemContactBinding
import com.alvaroestrada.randomuser.models.ContactView
import com.bumptech.glide.Glide

class ContactsAdapter(private val onClick:(ContactView) -> Unit) : ListAdapter<ContactView, ContactsAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.binding.contactName.text = contact.fullName
        holder.binding.contactEmail.text = contact.email
        Glide.with(holder.itemView.context).load(contact.mediumPicUrl).circleCrop().into(holder.binding.contactImage)
        holder.itemView.setOnClickListener { onClick(contact) }
    }

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)
}