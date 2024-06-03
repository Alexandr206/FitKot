package com.example.fitkot.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitkot.R

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Карта"


        val navigation_news: Button = findViewById(R.id.navigation_news)
        val navigation_map: Button = findViewById(R.id.navigation_map)
        val navigation_messages: Button = findViewById(R.id.navigation_messages)
        val navigation_trainers: Button = findViewById(R.id.navigation_trainers)
        val navigation_profile: Button = findViewById(R.id.navigation_profile)
        navigation_news.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }

        navigation_map.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        navigation_messages.setOnClickListener {
            startActivity(Intent(this, MessagesActivity::class.java))
        }

        navigation_trainers.setOnClickListener {
            startActivity(Intent(this, TrainersActivity::class.java))
        }

        navigation_profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
