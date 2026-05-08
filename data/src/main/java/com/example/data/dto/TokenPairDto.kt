package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenPairDto(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String
)
