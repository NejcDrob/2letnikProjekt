package com.example.drivingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.app.*

import com.example.app.databinding.FragmentLogInBinding
import com.mongodb.MongoException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.bson.Document
import org.json.JSONObject
import java.io.IOException


class LogInFragment:Fragment(R.layout.fragment_log_in) {
    private lateinit var binding: FragmentLogInBinding
    private val client = OkHttpClient()
    val email = "t"
    lateinit var myApplication: MyApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication
        myApplication.user=Document()
        myApplication.loggedIn=false


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupButton.setOnClickListener {
            val registerFragment = RegisterFragment()


            // Get the parent activity's fragment manager
            val fragmentManager = requireActivity().supportFragmentManager

            // Begin the fragment transaction
            fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, registerFragment)
                .addToBackStack(null)
                .commit()
            // Replace the current fragment with the new fragment

        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            println("button clicked")
            lifecycleScope.launch(Dispatchers.Default) {

            val state= myApplication.login(username, password)
            if(state==0)
                {
                    val scanFragment = ScanFragment()


                    // Get the parent activity's fragment manager
                    val fragmentManager = requireActivity().supportFragmentManager

                    // Begin the fragment transaction
                    fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, scanFragment)
                        .addToBackStack(null)
                        .commit()
                    // Replace the current fragment with the new fragment
                }
                withContext(Dispatchers.Main) {
                    if(state==1)
                    {
                        activity?.runOnUiThread {
                            // Show error message to the user
                            showError("username or password is incorrect")
                        }
                    }
                    println("got here")
                }
                println("button clicked")
           }

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

    private fun oldlogin(username: String, password: String) {
        lifecycleScope.launch(Dispatchers.Default) {
            var mongoClient: MongoClient? = null
            try {
                mongoClient = MongoClients.create("mongodb://192.168.0.105:27017")
               // println("A!")
                val database = mongoClient.getDatabase("vaja4")
               // println("B!")
                val collection: MongoCollection<Document> = database.getCollection("users")
                println("C!")
                println(collection.find(eq("username","tt")).first())
                println(collection.find())
               // if(BCrypt.checkpw(()))
             //   println(collection.find().toList())
                //val doc: Document = collection.find(eq("title", "Back to the Future")).first()
                println("Kotlin is now connected to MongoDB!")
            } catch (e: MongoException) {
                e.printStackTrace()
            } finally {
                mongoClient!!.close()

            }

            withContext(Dispatchers.Main) {

            }
        }

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
