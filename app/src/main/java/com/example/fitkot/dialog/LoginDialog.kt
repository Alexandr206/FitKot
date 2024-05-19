package com.example.fitkot.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fitkot.R
import com.example.fitkot.activity.ProfileActivity
import com.example.fitkot.database.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginDialog(context: Context, private val userRepository: UserRepository) {
    private val dialog: AlertDialog
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.login_dialog, null)

        val emailInput: EditText = view.findViewById(R.id.emailInput)
        val passwordInput: EditText = view.findViewById(R.id.passwordInput)
        val confirmButton: Button = view.findViewById(R.id.confirmButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        builder.setView(view)
        dialog = builder.create()

        confirmButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            scope.launch {
                userRepository.getUserByEmailAndPassword(email, password).collect { user ->
                    if (user != null) {
                        val intent = Intent(context, ProfileActivity::class.java)
                        intent.putExtra("userId", user.id)
                        context.startActivity(intent)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                    }
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
