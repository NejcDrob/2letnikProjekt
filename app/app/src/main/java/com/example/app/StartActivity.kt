package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.drivingapp.MainActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val buttonStart: Button = findViewById(R.id.button)
        buttonStart.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}