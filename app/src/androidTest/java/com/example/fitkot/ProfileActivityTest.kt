package com.example.fitkot.activity

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.fitkot.R
import com.example.fitkot.database.AppDatabase
import com.example.fitkot.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProfileActivityTest {

    private lateinit var db: AppDatabase
    private lateinit var scenario: ActivityScenario<ProfileActivity>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = AppDatabase.getDatabase(context)
        val user = User(id = 1, email = "test@test.com", fullName = "Test User", password = "password", role = "Пользователь")
        runBlocking {
            db.userDao().insert(user)
        }
        val intent = Intent(context, ProfileActivity::class.java).putExtra("userId", 1)
        scenario = ActivityScenario.launch(intent)
    }

    @After
    fun tearDown() {
        runBlocking {
            db.userDao().delete(db.userDao().getUserById(1).first())
        }
        scenario.close()
    }

    @Test
    fun displaysUserData() {
        onView(withId(R.id.emailTextView)).check(matches(withText("Email: test@test.com")))
        onView(withId(R.id.fullNameTextView)).check(matches(withText("ФИО: Test User")))
        onView(withId(R.id.roleTextView)).check(matches(withText("Роль: Пользователь")))
    }
}
