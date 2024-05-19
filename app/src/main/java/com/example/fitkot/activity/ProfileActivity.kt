package com.example.fitkot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.fitkot.database.UserRepository
import com.example.fitkot.models.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ProfileActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository
    private val scope = CoroutineScope(Dispatchers.Main) // Scope для корутины

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userRepository = UserRepository(this)

        val userId = intent.getIntExtra("userId", 0)

        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val fullNameTextView: TextView = findViewById(R.id.fullNameTextView)
        val roleTextView: TextView = findViewById(R.id.roleTextView)

        scope.launch {
            userRepository.getUserById(userId).collect { user ->
                emailTextView.text = "Email: ${user.email}"
                fullNameTextView.text = "ФИО: ${user.fullName}"
                roleTextView.text = "Роль: ${user.role}"
            }
        }
    }
}