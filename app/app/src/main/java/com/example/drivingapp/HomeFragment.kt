package com.example.drivingapp

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.databinding.FragmentHomeBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val client = OkHttpClient()
    val email = "t"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            signUp(username, password, email)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            login(username, password)
        }
    }

    private fun signUp(username: String, password: String, email: String) {
        val requestBody = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("email", email)
        }

        val request = Request.Builder()
            .url("http://localhost:3000/register")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody.toString()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle network errors
                activity?.runOnUiThread {
                    // Show error message to the user
                    showError("Sign up failed. Please try again later.")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                if (response.isSuccessful) {
                    // Sign up successful
                    activity?.runOnUiThread {
                        // Show success message or navigate to the next screen
                        showSuccess("Sign up successful!")
                    }
                } else {
                    // Sign up failed
                    val errorResponse = JSONObject(responseData)
                    val errorMessage = errorResponse.optString("message", "Unknown error occurred.")

                    activity?.runOnUiThread {
                        // Show error message to the user
                        showError(errorMessage)
                    }
                }
            }
        })
    }

    private fun login(username: String, password: String) {
        val requestBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val request = Request.Builder()
            .url("http://localhost:3000/login")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody.toString()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle network errors
                activity?.runOnUiThread {
                    // Show error message to the user
                    showError("Login failed. Please try again later.")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                if (response.isSuccessful) {
                    // Login successful
                    val user = JSONObject(responseData)

                    activity?.runOnUiThread {
                        // Handle the logged-in user data or navigate to the next screen
                        handleLoginSuccess(user)
                    }
                } else {
                    // Login failed
                    val errorResponse = JSONObject(responseData)
                    val errorMessage = errorResponse.optString("message", "Unknown error occurred.")

                    activity?.runOnUiThread {
                        // Show error message to the user
                        showError(errorMessage)
                    }
                }
            }
        })
    }

    private fun showError(errorMessage: String) {
        // Show error message to the user (e.g., using a TextView)
        binding.errorMessageTextView.text = errorMessage
        binding.errorMessageTextView.visibility = View.VISIBLE
    }

    private fun showSuccess(successMessage: String) {
        // Show success message to the user (e.g., using a TextView)
        binding.successMessageTextView.text = successMessage
        binding.successMessageTextView.visibility = View.VISIBLE
    }

    private fun handleLoginSuccess(user: JSONObject) {
        // Handle the logged-in user data or navigate to the next screen
        // Example: Extract user details and store them in shared preferences
        val userId = user.optString("id", "")
        val username = user.optString("username", "")

        // Store user data in shared preferences for future use
        val sharedPrefs = context?.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        sharedPrefs?.edit()?.apply {
            putString("userId", userId)
            putString("username", username)
            apply()
        }

        // Example: Navigate to the home screen
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
