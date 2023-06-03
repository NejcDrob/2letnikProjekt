package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.app.databinding.ActivityMainBinding
import com.example.drivingapp.LogInFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val logInFragment = LogInFragment()
        val scanFragment = ScanFragment()
        val profileFragment = ProfileFragment()
        //binding.bottomNavigationView.visibility= View.GONE

        setCurrentFragment(logInFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navScan ->setCurrentFragment(scanFragment)
                R.id.navProfile ->setCurrentFragment(profileFragment)
            }
            true

        }
        fun ttt()
        {

        }
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment,fragment)
            commit()
        }
    }
}