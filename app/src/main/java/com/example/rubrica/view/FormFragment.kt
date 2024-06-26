package com.example.rubrica.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.rubrica.R
import com.example.rubrica.databinding.FormFragmentBinding
import com.example.rubrica.model.Contact
import com.example.rubrica.viewmodel.RubricaViewModel
import com.google.android.material.snackbar.Snackbar

class FormFragment : Fragment() {

    internal data class Fields(val name: String, val surname: String, val number: String, val color: Int) {

        val avatarIconText: String
            get() {
                val n = name.first().uppercase()
                val s = surname.first().uppercase()
                return "$n$s"
            }

        fun isValidFields(): Boolean {
            return (name.isNotEmpty() && surname.isNotEmpty() && number.isNotEmpty())
        }

        fun isValidAvatarField(): Boolean {
            return (name.isNotEmpty() && surname.isNotEmpty())
        }
    }

    private var _binding: FormFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var view: View

    private  lateinit var navController: NavController

    private val viewModel: RubricaViewModel by activityViewModels()

    private var iconColorRes: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FormFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view
        navController = findNavController()
        val contact = viewModel.getCurrentContact()
        if (contact != null) configContactLayout(contact)
        else configNewContactLayout()
        configureDynamicIconName()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun  configContactLayout(contact: Contact) {
        binding.iconName.text = contact.getIconName
        iconColorRes = contact.getIconColor
        binding.iconName.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context,contact.getIconColor))
        binding.nameInputText.setText(contact.getName)
        binding.surnameInputText.setText(contact.getSurname)
        binding.numberInputText.setText(contact.getNumber)
        binding.saveButton.setOnClickListener {
            editContact(contact)
        }
        binding.deleteButton.setOnClickListener {
            deleteContact(contact)
        }
    }

    private  fun  configNewContactLayout() {
        binding.deleteButton.visibility = View.GONE
        binding.saveButton.setOnClickListener {
            createNewContact()
        }
    }

    private fun configureDynamicIconName() {
        binding.nameInputText.addTextChangedListener {
            val fields = getFields()
            if (fields.isValidAvatarField()) {
                binding.iconName.text = fields.avatarIconText
                val color = ContextCompat.getColor(binding.root.context, fields.color)
                binding.iconName.backgroundTintList = ColorStateList.valueOf(color)
            } else {
                binding.iconName.text = ContextCompat.getString(binding.root.context,R.string.undefinedAvatarName)
            }
        }
        binding.surnameInputText.addTextChangedListener {
            val fields = getFields()
            if (fields.isValidAvatarField()) {
                binding.iconName.text = fields.avatarIconText
                val color = ContextCompat.getColor(binding.root.context, fields.color)
                binding.iconName.backgroundTintList = ColorStateList.valueOf(color)
            }
        }
    }

    private fun createNewContact() {
        val fields = getFields()

        if(fields.isValidFields()) {
            viewModel.addNewContact(createContactFrom(fields))
            navController.navigateUp()
        } else {
            Snackbar.make(view,"No Valid", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun editContact(contact: Contact) {
        val fields = getFields()

        if(fields.isValidFields()) {
            viewModel.editContact(contact.id,createContactFrom(fields))
            navController.navigateUp()
        } else {
            Snackbar.make(view,"No Valid", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun deleteContact(contact: Contact) {
        viewModel.deleteContact(contact)
        navController.navigateUp()
    }

    private fun getFields(): FormFragment.Fields {
        val name = binding.nameInputText.text.toString()
        val surname = binding.surnameInputText.text.toString()
        val number = binding.numberInputText.text.toString()
        if(iconColorRes == null) iconColorRes = getRandomAvatarColor()
        val color: Int = iconColorRes ?: getRandomAvatarColor()
        return  FormFragment.Fields(name,surname,number, color)
    }

    private fun createContactFrom(fields: FormFragment.Fields): Contact =  Contact(name = fields.name, surname = fields.surname, number = fields.number, fields.color)

    private fun getRandomAvatarColor(): Int  {
        val colors: List<Int> = listOf(
            R.color.avatar_icon_1,
            R.color.avatar_icon_2,
            R.color.avatar_icon_3,
        )
        return colors.random()
    }
}