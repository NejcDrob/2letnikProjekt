package com.example.drivingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.ScanFragment
import com.example.app.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment: Fragment(R.layout.fragment_register) {
    lateinit var myApplication: MyApplication
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val mail = binding.mailEditText.text.toString()
            if(username=="" || password=="" || mail=="")
            {
                activity?.runOnUiThread {
                    // Show error message to the user
                    showError("1 or more fields are empty")
                }
            }
            else {
                println("register button pressed");
                lifecycleScope.launch(Dispatchers.Default) {
                    val state = myApplication.signUp(username, password, mail)
                    if (state == 0) {
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
                            showError("error, user already exists")
                        }
                        }
                    }
                }

            }
        }
    }
    fun showError(errorMessage: String) {
        // Show error message to the user (e.g., using a TextView)
        binding.errorMessageTextView.text = errorMessage
        binding.errorMessageTextView.visibility = View.VISIBLE
    }
}
