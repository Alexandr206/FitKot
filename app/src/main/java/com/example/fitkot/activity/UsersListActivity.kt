package com.example.fitkot.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitkot.R
import com.example.fitkot.database.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class UsersListActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var usersLayout: LinearLayout // Объявляем usersLayout как член класса

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        userRepository = UserRepository(this)

        usersLayout = findViewById(R.id.usersLayout) // Инициализируем usersLayout

        scope.launch {
            try {
                userRepository.getAllUsers().collect { users ->
                    usersLayout.removeAllViews() // Очищаем список перед обновлением
                    users.forEach { user ->
                        val userBlock = LinearLayout(this@UsersListActivity)
                        userBlock.orientation = LinearLayout.VERTICAL
                        val emailTextView = TextView(this@UsersListActivity)
                        emailTextView.text = "Email: ${user.email}"
                        val fullNameTextView = TextView(this@UsersListActivity)
                        fullNameTextView.text = "ФИО: ${user.fullName}"
                        userBlock.addView(emailTextView)
                        userBlock.addView(fullNameTextView)
                        userBlock.setOnClickListener {
                            showUserDetailsDialog(user)
                        }
                        usersLayout.addView(userBlock)
                    }
                }
            } catch (e: Exception) {
                Log.e("UsersListActivity", "Ошибка при получении списка пользователей", e)
                Toast.makeText(this@UsersListActivity, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUserDetailsDialog(user: com.example.fitkot.models.User) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Детали пользователя")
        builder.setMessage(
            "ID: ${user.id}\n" +
                    "Email: ${user.email}\n" +
                    "ФИО: ${user.fullName}\n" +
                    "Пароль: ${user.password}\n" +
                    "Роль: ${user.role}"
        )
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.setNeutralButton("Удалить") { dialog, _ ->
            scope.launch {
                try {
                    userRepository.deleteUser(user)
                    // Обновляем список пользователей после удаления
                    userRepository.getAllUsers().collect { users ->
                        usersLayout.removeAllViews()
                        users.forEach { updatedUser ->
                            val userBlock = LinearLayout(this@UsersListActivity)
                            userBlock.orientation = LinearLayout.VERTICAL
                            val emailTextView = TextView(this@UsersListActivity)
                            emailTextView.text = "Email: ${updatedUser.email}"
                            val fullNameTextView = TextView(this@UsersListActivity)
                            fullNameTextView.text = "ФИО: ${updatedUser.fullName}"
                            userBlock.addView(emailTextView)
                            userBlock.addView(fullNameTextView)
                            userBlock.setOnClickListener {
                                showUserDetailsDialog(updatedUser)
                            }
                            usersLayout.addView(userBlock)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("UsersListActivity", "Ошибка при удалении пользователя", e)
                    Toast.makeText(this@UsersListActivity, "Ошибка при удалении пользователя", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel() // Отменяем корутины при уничтожении Activity
    }
}
