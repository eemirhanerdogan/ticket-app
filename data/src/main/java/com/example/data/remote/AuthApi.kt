package com.example.data.remote

import com.example.data.dto.CredentialsDto
import com.example.data.dto.RefreshRequestDto
import com.example.data.dto.TokenPairDto
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit ile spesifik bir API'e istek atan kodu yazdık.
interface AuthApi {
    @POST("/auth/login")
    suspend fun login(@Body body: CredentialsDto): TokenPairDto

    @POST("/auth/register")
    suspend fun register(@Body body: CredentialsDto): TokenPairDto

    @POST("/auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): TokenPairDto
}
