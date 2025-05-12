package com.example.tamaleshr.service.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.joda.time.DateTime

@JsonClass(generateAdapter = true)
data class JwtTokenPayload(
    val sub: String?,
    @Json(name = "preferred_username")
    val username: String?,
    val email: String?,
    val exp: Long?,
    val iat: Long?,
    @Json(name = "realm_access")
    val realmAccess: Access,
    @Json(name = "resource_access")
    val resourceAccess: ResourceAccess? = null
) {
    val role: Role
        get() = Role.from(resourceAccess?.clientAccess?.roles?.firstOrNull().orEmpty())
            ?: Role.EMPLOYEE
}

@JsonClass(generateAdapter = true)
data class ResourceAccess(
    @Json(name = "tamalesHr-rest-api")
    val clientAccess: Access? = null
)

@JsonClass(generateAdapter = true)
data class Access(
    val roles: List<String>?
)

enum class Role(val key: String) {
    EMPLOYEE("Employee"),
    MANAGER("Manager");

    companion object {
        fun from(key: String): Role? {
            return entries.firstOrNull { it.key == key }
        }
    }
}


