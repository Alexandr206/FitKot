package com.example.fitkot

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.fitkot.activity.UsersListActivity
import com.example.fitkot.database.AppDatabase
import com.example.fitkot.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class UsersListActivityTest {

    private lateinit var db: AppDatabase
    private lateinit var scenario: ActivityScenario<UsersListActivity>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = AppDatabase.getDatabase(context)
        val user = User(id = 1, email = "test@test.com", fullName = "Test User", password = "password", role = "Пользователь")
        runBlocking {
            db.userDao().insert(user)
        }
        scenario = ActivityScenario.launch(UsersListActivity::class.java)
    }

    @After
    fun tearDown() {
        runBlocking {
            db.userDao().delete(db.userDao().getUserById(1).first())
        }
        scenario.close()
    }

    @Test
    fun displaysUsersList() {
        onView(withText("Email: test@test.com")).check(matches(isDisplayed()))
        onView(withText("ФИО: Test User")).check(matches(isDisplayed()))
    }

    @Test
    fun showsUserDetailsDialog() {
        onView(withText("Email: test@test.com")).perform(click())
        onView(withText("Детали пользователя")).check(matches(isDisplayed()))
    }
}
