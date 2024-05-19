package com.example.fitkot.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitkot.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User>

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): Flow<User>

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User>
}