package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    val error: String? = null,
    val message: String? = null
)
