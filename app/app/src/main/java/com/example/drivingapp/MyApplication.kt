package com.example.drivingapp

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        println("myapplication has been made")
    }
    fun test()
    {
        println("test")
    }
}