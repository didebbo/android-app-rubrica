package com.example.rubrica.view

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.rubrica.R
import com.example.rubrica.databinding.ContactItemBinding
import com.example.rubrica.model.Contact

class ContactAdapter(
    private val onCallNumber: ((Contact) -> Unit)?,
    private val onEditContact: ((Contact) -> Unit)?,
): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private val contactList: MutableList<Contact> = mutableListOf()

    inner class ViewHolder(private val contactItemBinding: ContactItemBinding): RecyclerView.ViewHolder(contactItemBinding.root) {
        private var iconName = contactItemBinding.iconName
        private var fullName = contactItemBinding.fullName
        private var number = contactItemBinding.number

        fun bind(contact: Contact) {
            iconName.text = contact.getIconName
            val color = ContextCompat.getColor(contactItemBinding.root.context, contact.getIconColor)
            iconName.backgroundTintList = ColorStateList.valueOf(color)
            fullName.text = contact.getFullName
            number.text = contact.getNumber
            contactItemBinding.callNumberButton.setOnClickListener {
                onCallNumber?.invoke(contact)
            }
            contactItemBinding.editNumberButton.setOnClickListener {
                onEditContact?.invoke(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bind(contact)
    }

    fun updateContactList(newList: MutableList<Contact>) {
        newList.forEach{ println("[GN] ${it.id} $it") }
        contactList.clear()
        contactList.addAll(newList)
        notifyDataSetChanged()
    }

}