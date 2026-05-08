package com.example.data.repository

import com.example.core.domain.AuthRepository
import com.example.core.domain.AuthSession
import com.example.core.domain.User
import com.example.core.domain.UserRole
import com.example.data.dto.CredentialsDto
import com.example.data.remote.AuthApi
import com.example.data.util.runCatchingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {
    override val isLoggedIn: Flow<Boolean>
        get() = flowOf(false)

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.login(CredentialsDto(email = email, password = password))
    }.onSuccess {
        // jwt'i bi yere yaz..
    }.map { i ->
        AuthSession(
            user = User(
                i.user.id, i.user.email, UserRole.fromApi(i.user.role),
            ),
            accessToken = i.accessToken,
            refreshToken = i.refreshToken
        )
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<AuthSession> {
        return Result.failure(Exception("Register is not implemented yet"))
    }

    override suspend fun logout(): Result<Unit> {
        return Result.success(Unit)
    }
}
