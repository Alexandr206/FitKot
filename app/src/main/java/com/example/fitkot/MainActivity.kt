package com.example.fitkot

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitkot.database.UserRepository
import com.example.fitkot.models.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userRepository = UserRepository(this)

        val loginButton: Button = findViewById(R.id.loginButton)
        val registrationButton: Button = findViewById(R.id.registrationButton)

        loginButton.setOnClickListener {
            val loginDialog = LoginDialog(this, userRepository)
            loginDialog.show()
        }

        registrationButton.setOnClickListener {
            val registrationDialog = RegistrationDialog(this, userRepository)
            registrationDialog.show()
        }

        // Добавляем тестовую запись администратора
        lifecycleScope.launch {
            val adminUser = User(email = "Admin@mail.ru", fullName = "Иван Иванович Иванов", password = "admin", role = "Администратор")
            userRepository.insertUser(adminUser)
        }
    }
}