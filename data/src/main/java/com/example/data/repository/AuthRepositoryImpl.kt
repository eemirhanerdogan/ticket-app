package com.example.data.repository

import com.example.core.domain.auth.AuthRepository
import com.example.core.domain.auth.AuthSession
import com.example.core.domain.auth.User
import com.example.core.domain.auth.UserRole
import com.example.data.dto.auth.CredentialsDto
import com.example.data.local.TokenStore
import com.example.data.remote.AuthApi
import com.example.data.util.runCatchingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {
    override val isLoggedIn: Flow<Boolean>
        get() = tokenStore.accessToken.map { it != null }

    override val userRole: Flow<UserRole?>
        get() = tokenStore.userRole.map { it?.let { role -> UserRole.fromApi(role) } }

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.login(CredentialsDto(email = email, password = password))
    }.onSuccess { tokenPairDto ->
        tokenStore.save(tokenPairDto.accessToken, tokenPairDto.refreshToken, tokenPairDto.user.role)
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
    ): Result<AuthSession> = runCatchingApi {
        authApi.register(CredentialsDto(email = email, password = password))
    }.onSuccess { tokenPairDto ->
        tokenStore.save(tokenPairDto.accessToken, tokenPairDto.refreshToken, tokenPairDto.user.role)
    }.map { i ->
        AuthSession(
            user = User(
                i.user.id, i.user.email, UserRole.fromApi(i.user.role),
            ),
            accessToken = i.accessToken,
            refreshToken = i.refreshToken
        )
    }

    override suspend fun logout(): Result<Unit> {
        tokenStore.clear()
        return Result.success(Unit)
    }
}
