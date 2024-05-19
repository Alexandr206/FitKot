package com.example.fitkot.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fitkot.R
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
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        builder.setView(view)
        dialog = builder.create()

        confirmButton.setOnClickListener {
            val email = emailInput.text.toString()
            val fullName = fullNameInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (email.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            scope.launch {
                userRepository.getUserByEmail(email).collect { existingUser ->
                    if (existingUser != null) {
                        Toast.makeText(context, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show()
                        return@collect
                    }

                    val newUser = User(email = email, fullName = fullName, password = password, role = "Пользователь")
                    userRepository.insertUser(newUser)

                    Toast.makeText(context, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}
