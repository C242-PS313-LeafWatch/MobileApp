package com.example.capstone.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.capstone.R
import com.example.capstone.data.Result
import com.example.capstone.databinding.FragmentCameraBinding
import com.example.capstone.ui.utils.getImageUri
import com.example.capstone.ui.result.ResultActivity
import com.example.capstone.ui.utils.ViewModelFactory
import com.example.capstone.ui.utils.reduceFileImage
import com.example.capstone.ui.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class CameraFragment : Fragment() {

    private val viewModel: CameraViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBtnGalery.setOnClickListener { startGallery() }
        binding.imgBtnCamera.setOnClickListener { startCamera() }
        binding.btnAnalyze.setOnClickListener { analyzeImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireActivity())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("CameraFragment", "showImage: $it")
            binding.imgViewScan.setImageURI(it)
        }
    }

    private fun observeViewModel(file: MultipartBody.Part, currentImg: Uri?) {
        viewModel.uploadFile(file, currentImg).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    showToast(result.data.status)
                    val prediction = result.data.prediction
                    val videoList = result.data.videos
                    val intent = Intent(requireActivity(), ResultActivity::class.java)
                    intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri)
                    intent.putExtra(ResultActivity.EXTRA_PREDICTION, prediction)
                    intent.putParcelableArrayListExtra(ResultActivity.EXTRA_LIST_VIDEO, ArrayList(videoList))
                    Log.d(TAG, "startIntent")
                    startActivity(intent)
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                    currentImageUri = null
                    binding.imgViewScan.setImageResource(R.drawable.ic_place_holder)
                }
            }
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireActivity()).reduceFileImage()
            Log.d(TAG, "uploadImage: ${imageFile.path}")
            showLoading(true)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            observeViewModel(multipartBody, currentImageUri)
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading)  View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "CameraFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}