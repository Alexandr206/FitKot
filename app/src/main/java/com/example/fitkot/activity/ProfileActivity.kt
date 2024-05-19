package com.example.fitkot.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitkot.MainActivity
import com.example.fitkot.R
import com.example.fitkot.database.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userRepository = UserRepository(this)

        val userId = intent.getIntExtra("userId", 0)

        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val fullNameTextView: TextView = findViewById(R.id.fullNameTextView)
        val roleTextView: TextView = findViewById(R.id.roleTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val usersListButton: Button = findViewById(R.id.usersListButton)

        scope.launch {
            try {
                userRepository.getUserById(userId).collect { user ->
                    emailTextView.text = "Email: ${user.email}"
                    fullNameTextView.text = "ФИО: ${user.fullName}"
                    roleTextView.text = "Роль: ${user.role}"
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Ошибка при получении пользователя", e)
                Toast.makeText(this@ProfileActivity, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Закрываем ProfileActivity
        }

        usersListButton.setOnClickListener {
            val intent = Intent(this, UsersListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel() // Отменяем корутины при уничтожении Activity
    }
}
