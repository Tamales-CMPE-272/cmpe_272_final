package com.example.tamaleshr.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tamaleshr.TestApplication
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.startKoinMock
import com.example.tamaleshr.ui.home.HomeFragment
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.example.tamaleshr.R as TamalesR

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class HomeFragmentTest: KoinTest {

    val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        startKoinMock(
            object: DispatcherProvider {
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

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testHomeFragmentUI() = runTest(testDispatcher) {
        val navController = mockk<NavController>(relaxed = true)

        launchFragmentInContainer<HomeFragment>(
            themeResId = TamalesR.style.Theme_TamalesHR,
        ).onFragment { fragment ->
            // Set up mock nav controller
            Navigation.setViewNavController(fragment.requireView(), navController)

            // Test Title
            testDispatcher.scheduler.advanceUntilIdle()
            val welcomeText = fragment.binding.tvTitle.text.toString()
            assertThat(welcomeText).isEqualTo("Welcome Napoleon!")

            // Test Profile button
            // Simulate click on Profile section
            fragment.binding.clProfile.performClick()
            verify { navController.navigate(TamalesR.id.nav_profile) }

            // Simulate click on Salary section
            fragment.binding.clSalary.performClick()
            verify { navController.navigate(TamalesR.id.nav_salary) }

            // Simulate click on Manage section (only visible if Manager)
            fragment.binding.clManage.performClick()
            verify { navController.navigate(TamalesR.id.nav_department) }

            fragment.onDestroyView()
            // Test view detaching
            assertThat(fragment._binding).isNull()
        }
    }

    @After
    fun after(){
        stopKoin()
    }
}