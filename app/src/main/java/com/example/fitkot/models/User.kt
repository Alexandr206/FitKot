package com.example.fitkot.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val fullName: String,
    val password: String,
    val role: String
)