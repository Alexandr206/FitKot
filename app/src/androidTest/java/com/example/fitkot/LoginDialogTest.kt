package com.example.fitkot.dialog

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.fitkot.R
import com.example.fitkot.activity.ProfileActivity
import com.example.fitkot.database.AppDatabase
import com.example.fitkot.database.UserRepository
import com.example.fitkot.models.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginDialogTest {

    private lateinit var db: AppDatabase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = AppDatabase.getDatabase(context)
        userRepository = UserRepository(context)
        runBlocking {
            db.userDao().insert(User(email = "test@test.com", fullName = "Test User", password = "test", role = "Пользователь"))
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun successfulLogin() {
        val scenario = ActivityScenario.launch(ProfileActivity::class.java)
        scenario.onActivity { activity ->
            val dialog = LoginDialog(activity, userRepository)
            dialog.show()

            onView(withId(R.id.emailInput))
                .inRoot(isDialog())
                .perform(typeText("test@test.com"), closeSoftKeyboard())
            onView(withId(R.id.passwordInput))
                .inRoot(isDialog())
                .perform(typeText("test"), closeSoftKeyboard())
            onView(withId(R.id.confirmButton))
                .inRoot(isDialog())
                .perform(click())

            // Проверка, что ProfileActivity запущена с правильным userId
            val expectedIntent = Intent(activity, ProfileActivity::class.java).apply {
                putExtra("userId", 1)
            }
            //assertThat(shadowOf(activity).nextStartedActivity, equalTo(expectedIntent))
        }
    }

    @Test
    fun failedLogin() {
        val scenario = ActivityScenario.launch(ProfileActivity::class.java)
        scenario.onActivity { activity ->
            val dialog = LoginDialog(activity, userRepository)
            dialog.show()

            onView(withId(R.id.emailInput))
                .inRoot(isDialog())
                .perform(typeText("wrong@email.com"), closeSoftKeyboard())
            onView(withId(R.id.passwordInput))
                .inRoot(isDialog())
                .perform(typeText("wrong"), closeSoftKeyboard())
            onView(withId(R.id.confirmButton))
                .inRoot(isDialog())
                .perform(click())

            onView(withText("Неверный email или пароль"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }
}
