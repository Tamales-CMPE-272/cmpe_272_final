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
        return try {
            val profile = provider.service().fetchProfileById(id)
            Response.success(profile)
        } catch (e: Exception) {
            Response.error(500, okhttp3.ResponseBody.create(null, "Error fetching profile"))
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