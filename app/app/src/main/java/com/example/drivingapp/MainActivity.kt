package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.app.databinding.ActivityMainBinding
import com.example.drivingapp.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val homeFragment = HomeFragment();
        val scanFragment = ScanFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navHome ->setCurrentFragment(homeFragment)
                R.id.navScan ->setCurrentFragment(scanFragment)
                R.id.navProfile ->setCurrentFragment(profileFragment)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    }
}