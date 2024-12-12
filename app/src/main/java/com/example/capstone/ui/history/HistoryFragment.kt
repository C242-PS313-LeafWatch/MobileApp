package com.example.capstone.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHistoryBinding
import com.example.capstone.ui.adapter.ListHistoryAdapter
import com.example.capstone.ui.utils.ViewModelFactory


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels{
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return  root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.deleteView.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setTitle(getString(R.string.delete))
                setMessage(getString(R.string.success_delete))
                setPositiveButton(getString(R.string.yes_alert)) { _, _ ->
                    viewModel.deleteAllPredictAndVideos()
                }
                setNegativeButton(getString(R.string.no_alert)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    private fun setupRecyclerView() {
        viewModel.getAllPredictAndVideos().observe(viewLifecycleOwner, {
            Log.d(TAG, "PredictAndVideos: $it")
            val adapter = ListHistoryAdapter(it)
            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                this.adapter = adapter
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val TAG = "HistoryFragment"
    }
}