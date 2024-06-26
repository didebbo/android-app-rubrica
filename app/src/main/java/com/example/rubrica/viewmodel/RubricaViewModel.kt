package com.example.rubrica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rubrica.model.Contact
import com.example.rubrica.view.EditFragment
import java.util.UUID

class RubricaViewModel(): ViewModel() {

    private val _contactList: MutableLiveData<MutableList<Contact>> = MutableLiveData<MutableList<Contact>>(
        mutableListOf(
            Contact("Gianluca","Napoletano","1234567890")
        )
    )
    val contactList: LiveData<MutableList<Contact>> get() = _contactList

    private var currentContact: Contact? = null

    fun getCurrentContact(): Contact? = currentContact

    fun  selectContact(contact: Contact): Contact?  {
        currentContact = contactList.value?.find { it.id == contact.id }
        return currentContact
    }

    fun  unSelectContact() {
        currentContact = null
    }

    fun addNewContact(newContact: Contact) {
        _contactList.value?.add(newContact)
        unSelectContact()
    }

    fun editContact(id: UUID , withNew: Contact) {
        _contactList.value?.apply {
            val index = indexOfFirst { it.id == id }
            if (index != -1) {
                this[index] = this[index].update(withNew)
            }
        }
        unSelectContact()
    }

    fun deleteContact(contact: Contact) {
        _contactList.value?.remove(contact)
        unSelectContact()
    }
}