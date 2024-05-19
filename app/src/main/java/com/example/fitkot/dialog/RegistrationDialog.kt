package com.example.fitkot

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fitkot.database.UserRepository
import com.example.fitkot.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationDialog(context: Context, private val userRepository: UserRepository) {
    private val dialog: AlertDialog
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.registration_dialog, null)

        val emailInput: EditText = view.findViewById(R.id.emailInput)
        val fullNameInput: EditText = view.findViewById(R.id.fullNameInput)
        val passwordInput: EditText = view.findViewById(R.id.passwordInput)
        val confirmPasswordInput: EditText = view.findViewById(R.id.confirmPasswordInput)
        val confirmButton: Button = view.findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener {
            val email = emailInput.text.toString()
            val fullName = fullNameInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Проверка ввода
            if (email.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Проверка наличия пользователя с таким email
            scope.launch {
                userRepository.getUserByEmail(email).collect { existingUser ->
                    if (existingUser != null) {
                        Toast.makeText(context, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show()
                        return@collect
                    }

                    // Создание нового пользователя
                    val newUser = User(email = email, fullName = fullName, password = password, role = "Пользователь")
                    userRepository.insertUser(newUser)

                    // Переход на страницу профиля
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                    //dialog.dismiss()
                }
            }
        }

        builder.setView(view)
        dialog = builder.create() // Инициализация dialog
    }

    fun show() {
        dialog.show()
    }
}