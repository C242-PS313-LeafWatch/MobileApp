package com.example.capstone.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.data.Result
import com.example.capstone.databinding.FragmentProfileBinding
import com.example.capstone.ui.utils.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupAction()
        setupView()
        return root
    }

    private fun setupAction() {
        binding.imgLogout.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle(getString(R.string.logout))
                setMessage(getString(R.string.success_logout))
                setPositiveButton(getString(R.string.yes_alert)) { _, _ ->
                    viewModel.logout()
                }
                setNegativeButton(getString(R.string.no_alert)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    private fun setupView() {
        viewModel.getProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    binding.apply {
                        nameTextView.text = result.data?.name
                        emailTextView.text = result.data?.email
                        genderTextView.text = result.data?.gender
                        birthTextView.text = result.data?.birthDate
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading)  View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}