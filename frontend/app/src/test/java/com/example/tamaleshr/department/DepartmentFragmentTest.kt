package com.example.tamaleshr.department

import android.content.DialogInterface
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import com.example.tamaleshr.TestApplication
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.startKoinMock
import com.example.tamaleshr.ui.department.DepartmentEmployeeAdapter
import com.example.tamaleshr.ui.department.DepartmentFragment
import com.google.android.material.button.MaterialButton
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowDialog
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class DepartmentFragmentTest : KoinTest {

    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        startKoinMock(
            dispatcherProvider = object : DispatcherProvider {
                override val io = testDispatcher
                override val main = testDispatcher
                override val default = testDispatcher
                override val unconfined = testDispatcher
            }
        )
    }

    @After
    fun after(){
        stopKoin()
    }

    @Test
    fun `department data loads and updates UI`() = runTest(testDispatcher) {
        launchFragmentInContainer<DepartmentFragment>(
            themeResId = com.example.tamaleshr.R.style.Theme_TamalesHR
        ).onFragment { fragment ->
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(fragment.binding.tvTitle.text.toString()).isEqualTo("Engineering Department")
            assertThat(fragment.binding.rvEmployees.adapter?.itemCount).isEqualTo(2)
        }
    }

    @Test
    fun `filter users updates UI`() = runTest(testDispatcher) {
        launchFragmentInContainer<DepartmentFragment>(
            themeResId = com.example.tamaleshr.R.style.Theme_TamalesHR
        ).onFragment { fragment ->
            testDispatcher.scheduler.advanceUntilIdle()

            // Clear List to avoid dealing with diff util
            (fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter).submitList(null)

            // Search call
            fragment.binding.etSearch.setText("1") // matches Alice (employeeNo = 1)
            fragment.binding.etSearch.onEditorAction(EditorInfo.IME_ACTION_DONE)
            testDispatcher.scheduler.advanceUntilIdle()

           // Set item count
            val adapter = fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter
            assertThat(adapter.itemCount).isEqualTo(1)
        }
    }

    @Test
    fun `add user flow triggers UI update`() = runTest(testDispatcher) {
        launchFragmentInContainer<DepartmentFragment>(
            themeResId = com.example.tamaleshr.R.style.Theme_TamalesHR
        ).onFragment { fragment ->

            testDispatcher.scheduler.advanceUntilIdle()
            fragment.binding.fabAddEmployee.performClick()

            testDispatcher.scheduler.advanceUntilIdle()

            val shownDialogs = ShadowDialog.getShownDialogs()
            assertThat(shownDialogs).isNotEmpty()

            val latestDialog = shownDialogs.last() as AlertDialog

            // Find EditText inside dialog and set value
            latestDialog.findViewById<EditText>(com.example.tamaleshr.R.id.etEmpNo)?.setText("123")
            latestDialog.getButton(DialogInterface.BUTTON_POSITIVE)?.performClick()

            shadowOf(Looper.getMainLooper()).idle()
            (fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter).submitList(null)

            // Advance coroutine dispatcher to process AddUserUseCase result
            testDispatcher.scheduler.advanceUntilIdle()

            // Verify RecyclerView updated with new user
            val adapter = fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter
            assertThat(adapter.itemCount).isEqualTo(3)
        }
    }

    @Test
    fun `remove user flow triggers UI update`() = runTest(testDispatcher) {
        launchFragmentInContainer<DepartmentFragment>(
            themeResId = com.example.tamaleshr.R.style.Theme_TamalesHR
        ).onFragment { fragment ->
            testDispatcher.scheduler.advanceUntilIdle()

            // Simulate clicking remove button on first employee item
            val adapter = fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter
            val viewHolder = adapter.onCreateViewHolder(fragment.binding.rvEmployees, -1)
            adapter.onBindViewHolder(viewHolder, 0)
            viewHolder.binding.btnRemove.performClick()

            // Retrieve the latest confirmation dialog
            val shownDialogs = ShadowDialog.getShownDialogs()
            assertThat(shownDialogs).isNotEmpty()

            val latestDialog = shownDialogs.last() as AlertDialog

            // Ensure dialog is displayed
            latestDialog.show()

            // Simulate clicking "Remove" (positive button)
            val positiveButton = latestDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            assertThat(positiveButton).isNotNull()

            positiveButton.performClick()

            // Ensure the ButtonHandler message dispatches
            shadowOf(Looper.getMainLooper()).idle()
            (fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter).submitList(null)

            // Advance coroutines for removeUser()
            testDispatcher.scheduler.advanceUntilIdle()

            // Verify RecyclerView updated
            val updatedAdapter = fragment.binding.rvEmployees.adapter as DepartmentEmployeeAdapter
            assertThat(updatedAdapter.itemCount).isEqualTo(1)
        }
    }

}