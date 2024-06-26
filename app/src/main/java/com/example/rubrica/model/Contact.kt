package com.example.rubrica.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rubrica.R
import com.example.rubrica.databinding.ContactItemBinding
import java.util.UUID


data class Contact(private var name: String, private var surname: String, private var number: String, private var color: Int = R.color.avatar_icon_default) {

    val id: UUID = UUID.randomUUID()
    val getName: String get() = name
    val getSurname: String get() = surname
    val getFullName: String get() = "$name $surname"
    val getNumber: String get() = number


    val getIconName: String
        get() {
            val n = name.first().uppercase()
            val s = surname.first().uppercase()
            return "$n$s"
        }
    val getIconColor: Int get() = color

    fun update(with: Contact) : Contact {
        name = with.getName
        surname = with.getSurname
        number = with.getNumber
        return this
    }
}