package com.example.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDto(val refreshToken: String)
