package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app.databinding.FragmentProfileBinding
import com.example.app.databinding.FragmentRegisterBinding
import com.example.app.databinding.FragmentScanBinding
import com.example.drivingapp.MyApplication

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
       var test= myApplication.TTT
        val name= myApplication.user.getString("username")
        //val email= myApplication.user.getString("email")
        binding.textViewEmail.text= "Email: $test"
        //binding.textViewEmail.text= "Email: $email"
        binding.textViewName.text="Name: $name"
        binding.buttonLogout.setOnClickListener {
            myApplication.connectToDatabase()
            var test= myApplication.TTT
            binding.textViewEmail.text= "Email: $test"
        }


    }


}