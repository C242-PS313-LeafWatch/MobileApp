package com.example.capstone.ui.subscription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.capstone.R
import com.example.capstone.databinding.FragmentSubscribeBinding

class SubscribeFragment : Fragment() {

    private var _binding: FragmentSubscribeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
}