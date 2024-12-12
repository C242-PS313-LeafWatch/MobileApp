package com.example.capstone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding
import com.example.capstone.ui.adapter.ListPlantAdapter
import com.example.capstone.ui.adapter.Plant

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var listPlantAdapter: ListPlantAdapter
    private val list = ArrayList<Plant>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.addAll(getListPlant())
        setupRecyclerView()
    }
    private fun getListPlant(): ArrayList<Plant> {
        val dataName = resources.getStringArray(R.array.data_nama)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)

        val listPlant = ArrayList<Plant>()
        for (i in dataName.indices) {
            val plant = Plant(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listPlant.add(plant)
        }
        return listPlant
    }
    private fun setupRecyclerView() {
        listPlantAdapter = ListPlantAdapter(list)
        binding.rvPlants.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvPlants.adapter = listPlantAdapter
        binding.rvPlants.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}