package com.example.fitkot.database

import android.content.Context
import androidx.room.Room
import com.example.fitkot.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserRepository(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "user_database"
    ).build()

    private val userDao = database.userDao()

    fun getUserByEmail(email: String): Flow<User> = userDao.getUserByEmail(email)

    fun getUserByEmailAndPassword(email: String, password: String): Flow<User> =
        userDao.getUserByEmailAndPassword(email, password)

    fun insertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insert(user)
        }
    }

    fun getUserById(userId: Int): Flow<User> = userDao.getUserById(userId)

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }
}
