package com.example.fitkot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitkot.models.User
import com.example.fitkot.models.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()

                // Добавляем тестового пользователя при создании базы данных
                CoroutineScope(Dispatchers.IO).launch {
                    val adminUser = User(email = "Admin@mail.ru", fullName = "Иван Иванович Иванов", password = "admin", role = "Администратор")
                    instance.userDao().insert(adminUser)
                }

                INSTANCE = instance
                instance
            }
        }
    }
}
