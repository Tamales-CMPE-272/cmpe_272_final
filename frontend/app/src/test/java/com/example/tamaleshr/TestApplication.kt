package com.example.tamaleshr

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.service.auth.AuthRepository
import com.example.tamaleshr.service.auth.AuthResponse
import com.example.tamaleshr.service.department.Department
import com.example.tamaleshr.service.department.DepartmentEmployee
import com.example.tamaleshr.service.department.DepartmentEmployeeData
import com.example.tamaleshr.service.department.DepartmentInfo
import com.example.tamaleshr.service.department.DepartmentManager
import com.example.tamaleshr.service.department.DepartmentRepository
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.service.employee.EmployeeRepository
import com.example.tamaleshr.util.AuthTokenManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import retrofit2.Response
import java.util.Date

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}

fun startKoinMock(
    dispatcherProvider: DispatcherProvider,
    tokenProvider: AuthTokenManager = FakeAuthTokenManager(ApplicationProvider.getApplicationContext())
) {
    startKoin {
        printLogger()
        modules(module {
            single<AuthTokenManager> { tokenProvider }
            single<EmployeeRepository> {
                mockEmployeeRepo()
            }
            single<AuthRepository> {
                mockAuthRepo()
            }
            single<DepartmentRepository> {
                mockDepartmentRepo()
            }
            single {
                dispatcherProvider
            }
        })
    }
}

private fun mockEmployeeRepo() = mockk<EmployeeRepository> {
    coEvery { findEmployeeById(any()) } answers {
        Response.success(
            Employee(
                emp_no = 123,
                first_name = "Napoleon",
                last_name = "Salazar",
                hire_date = Date(),
                birth_date = Date()
            )
        )
    }
}

private fun mockAuthRepo() = mockk<AuthRepository>(relaxed = true) {
    coEvery {
        authToken(any(), any(), any(), any())
    } returns Response.success(
        AuthResponse(
            access_token = "dummy-access-token",
            expires_in = 3600,
            refresh_expires_in = 1800,
            refresh_token = "dummy-refresh-token",
            token_type = "Bearer",
            session_state = "dummy-session-state",
            scope = "profile email"
        )
    )
}

private fun mockDepartmentRepo() = mockk<DepartmentRepository> {
    // Mock findDepartmentById
    coEvery { findDepartmentById(any()) } returns Response.success(
        Department(
            departmentInfo = DepartmentInfo(
                departmentNo = "d001",
                departmentName = "Engineering"
            ),
            departmentEmployees = listOf(
                DepartmentEmployee(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(
                        employeeNo = 1,
                        firsName = "Alice",
                        lastName = "Smith"
                    )
                ),
                DepartmentEmployee(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(
                        employeeNo = 2,
                        firsName = "Bob",
                        lastName = "Jones"
                    )
                )
            ),
            departmentManagers = listOf(
                DepartmentManager(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(
                        employeeNo = 10,
                        firsName = "Manager",
                        lastName = "Person"
                    )
                )
            )
        )
    )

    // Mock addUser
    coEvery { addUser(any(), any()) } returns Response.success(
        Department(
            departmentInfo = DepartmentInfo(
                departmentNo = "d001",
                departmentName = "Engineering"
            ),
            departmentEmployees = listOf(
                DepartmentEmployee(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(1, "Alice", "Smith")
                ),
                DepartmentEmployee(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(2, "Bob", "Jones")
                ),
                DepartmentEmployee(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(3, "Charlie", "Brown")
                )
            ),
            departmentManagers = listOf(
                DepartmentManager(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(10, "Manager", "Person")
                )
            )
        )
    )

    // Mock removeUser
    coEvery { removeUser(any(), any()) } returns Response.success(
        Department(
            departmentInfo = DepartmentInfo(
                departmentNo = "d001",
                departmentName = "Engineering"
            ),
            departmentEmployees = listOf(
                DepartmentEmployee(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(2, "Bob", "Jones")
                )
            ),
            departmentManagers = listOf(
                DepartmentManager(
                    currentlyEnrolled = true,
                    employee = DepartmentEmployeeData(10, "Manager", "Person")
                )
            )
        )
    )
}