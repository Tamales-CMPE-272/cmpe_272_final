package com.example.tamaleshr.service.profile

import com.example.tamaleshr.di.serviceModule
import com.example.tamaleshr.service.Repository
import com.example.tamaleshr.service.ServiceProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Response

class ProfileRepository(
    override val provider: ServiceProvider<ProfileService>
) : Repository<ProfileService> {

    suspend fun findEmployeeById(id: String): Response<Profile> {
        val response = provider.service().fetchGradesByAssignment(
            employeeId = id
        ).execute()
        return if (response.isSuccessful) {
            response
        } else {
            val error = response.errorBody()
                ?: throw IllegalAccessException("Response did not return error")
            Response.error(response.code(), error)
        }
    }

    companion object {
        fun koinModule(): Module {
            return module {
                includes(serviceModule)
                single {
                    ProfileRepository(
                        provider = ProfileServiceProvider()
                    )
                }
            }
        }
    }

}