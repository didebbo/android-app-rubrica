package com.example.rubrica.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubrica.R
import com.example.rubrica.databinding.RubricaFragmentBinding
import com.example.rubrica.viewmodel.RubricaViewModel
import com.google.android.material.snackbar.Snackbar

class RubricaFragment : Fragment() {

    private var _binding: RubricaFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel: RubricaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RubricaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.reciclerContactView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ContactAdapter(
            onCallNumber = {
                Snackbar.make(view,"Calling ${it.getFullName} at number: ${it.getNumber}",Snackbar.LENGTH_LONG).setAnchorView(binding.fab).show()
            },
            onEditContact = {
                viewModel.selectContact(it)
                navController.navigate(R.id.action_go_to_edit_fragment)
            },
        )

        binding.reciclerContactView.adapter = adapter

        viewModel.contactList.observe(viewLifecycleOwner) {
            it.let {
                adapter.updateContactList(it)
            }
            binding.noResultView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fab.setOnClickListener {
            viewModel.unSelectContact()
            navController.navigate(R.id.action_go_to_edit_fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}