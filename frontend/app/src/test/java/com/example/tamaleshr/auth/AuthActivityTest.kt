package com.example.tamaleshr.auth

import android.widget.Button
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import com.example.tamaleshr.TestApplication
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.startKoinMock
import com.example.tamaleshr.ui.auth.AuthActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import com.example.tamaleshr.R as TamalesR
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class AuthActivityTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoinMock(
            tokenProvider = mockk(relaxed = true){
                every { getAccessToken() } returns null
            },
            dispatcherProvider = object : DispatcherProvider {
                override val io: CoroutineDispatcher
                    get() = testDispatcher
                override val main: CoroutineDispatcher
                    get() = testDispatcher
                override val default: CoroutineDispatcher
                    get() = testDispatcher
                override val unconfined: CoroutineDispatcher
                    get() = testDispatcher
            }
        )
    }

    @Test
    fun testSuccessfulLoginNavigatesToMain() = runTest(testDispatcher) {
        ActivityScenario.launch(AuthActivity::class.java).onActivity { activity ->

            assertThat(activity.binding.btnLogin.isEnabled).isFalse()

            activity.binding.etPassword.setText("password123")
            activity.binding.etUsername.setText("napoleon")

            testScheduler.advanceUntilIdle()

            // Simulate clicking Login button
            assertThat(activity.binding.btnLogin.isEnabled).isTrue()
            activity.binding.btnLogin.performClick()

            testScheduler.advanceUntilIdle()

            val shadowActivity = shadowOf(activity)
            val startedIntent = shadowActivity.nextStartedActivity

            assertThat(startedIntent).isNotNull()

            val componentName = startedIntent.component
            assertThat(componentName?.className).isEqualTo("com.example.tamaleshr.ui.MainActivity")

        }
    }

    @After
    fun after(){
        stopKoin()
    }
}