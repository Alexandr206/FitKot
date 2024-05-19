package com.example.fitkot

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitkot.database.UserRepository
import com.example.fitkot.dialog.LoginDialog
import com.example.fitkot.dialog.RegistrationDialog

class MainActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userRepository = UserRepository(this)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            LoginDialog(this, userRepository).show()
        }

        val registrationButton: Button = findViewById(R.id.registrationButton) // Замените на ваш ID кнопки

        registrationButton.setOnClickListener {
            val registrationDialog = RegistrationDialog(this, userRepository)
            registrationDialog.show()
        }

    }
}
