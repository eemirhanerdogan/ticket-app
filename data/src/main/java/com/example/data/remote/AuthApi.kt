package com.example.data.remote

import com.example.data.dto.auth.CredentialsDto
import com.example.data.dto.auth.RefreshRequestDto
import com.example.data.dto.auth.TokenPairDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body body: CredentialsDto): TokenPairDto

    @POST("/auth/register")
    suspend fun register(@Body body: CredentialsDto): TokenPairDto

    @POST("/auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): TokenPairDto
}
