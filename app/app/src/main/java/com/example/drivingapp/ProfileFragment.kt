package com.example.drivingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app.databinding.FragmentProfileBinding
import com.example.drivingapp.LogInFragment
import com.example.drivingapp.MyApplication
import org.bson.Document

class ProfileFragment:Fragment(R.layout.fragment_profile) {
    lateinit var myApplication: MyApplication
    private lateinit var binding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireContext().applicationContext as MyApplication    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name= myApplication.user.getString("username")
        val email= myApplication.user.getString("email")
        if (!myApplication.loggedIn)
        {
            binding.buttonLogout.text="login"
        }
        binding.textViewEmail.text= "Email: $email"
        binding.textViewName.text="Name: $name"
        binding.buttonLogout.setOnClickListener {
            myApplication.user=Document()
            myApplication.loggedIn=false
            val logInFragment = LogInFragment()
            // Get the parent activity's fragment manager
            val fragmentManager = requireActivity().supportFragmentManager

            // Begin the fragment transaction
            fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, logInFragment)
                .addToBackStack(null)
                .commit()
            // Replace the current fragment with the new fragment
        }


    }


}