package com.example.capstone.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.example.capstone.data.Result
import com.example.capstone.databinding.ActivitySignupBinding
import com.example.capstone.ui.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)

        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val confPassword = ObjectAnimator.ofFloat(binding.confPasswordTextView, View.ALPHA, 1f).setDuration(500)
        val confPasswordEdit = ObjectAnimator.ofFloat(binding.confPasswordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val genderView = ObjectAnimator.ofFloat(binding.genderTextView, View.ALPHA, 1f).setDuration(500)
        val genderSpin = ObjectAnimator.ofFloat(binding.genderRadio, View.ALPHA, 1f).setDuration(500)
        val genderMale = ObjectAnimator.ofFloat(binding.male, View.ALPHA, 1f).setDuration(500)
        val genderFemale = ObjectAnimator.ofFloat(binding.female, View.ALPHA, 1f).setDuration(500)
        val birthView = ObjectAnimator.ofFloat(binding.birthView, View.ALPHA, 1f).setDuration(500)
        val birthTextView = ObjectAnimator.ofFloat(binding.birthTextView, View.ALPHA, 1f).setDuration(500)
        val birthIcon = ObjectAnimator.ofFloat(binding.btnCalendar, View.ALPHA, 1f).setDuration(500)
        val sign = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, name, nameEdit, email, emailEdit, password, passwordEdit, confPassword, confPasswordEdit, genderView, genderSpin, genderMale, genderFemale, birthView, birthTextView, birthIcon,sign)
            startDelay = 100
            start()
        }
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnCalendar.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    binding.birthTextView.text = formattedDate
                },
                year, month, day
            )
            datePickerDialog.show()
        }
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confPassword = binding.confPasswordEditText.text.toString()
            val selectedId = binding.genderRadio.checkedRadioButtonId
            val birth = binding.birthTextView.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.input_name)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.input_password)
                }
                confPassword.isEmpty() -> {
                    binding.confPasswordEditTextLayout.error = getString(R.string.input_confpass)
                }
                birth.isEmpty() -> {
                    binding.birthTextView.error = getString(R.string.input_birth)
                }
                else -> {
                    val selectedRadioButton = findViewById<RadioButton>(selectedId)
                    val gender = selectedRadioButton.text.toString()
                    observeViewModel(username = name, email = email, password = password, confPassword = confPassword, gender = gender, birth = birth)
                    Log.d(TAG, "setupAction: $name")
                    Log.d(TAG, "setupAction: $email")
                    Log.d(TAG, "setupAction: $password")
                    Log.d(TAG, "setupAction: $confPassword")
                    Log.d(TAG, "setupAction: $gender")
                    Log.d(TAG, "setupAction: $birth")
                }
            }
        }
    }
    private fun observeViewModel(username: String, email: String, password: String, confPassword: String, gender: String, birth: String) {
        viewModel.register(username = username, email = email, password = password, confPassword = confPassword, gender = gender, birthDate = birth).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    showToast(result.data.msg)
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.signup))
                        setMessage(getString(R.string.success_register))
                        setPositiveButton(getString(R.string.login)) { _, _ ->
                            finish()
                        }
                        create()
                        show()
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading)  View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "SIGNUP"
    }
}