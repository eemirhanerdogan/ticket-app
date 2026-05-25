package com.example.data.network

import com.example.data.dto.ErrorResponseDto

// Bağlantı kopuk, timeout, dns çözümleme
class NetworkException(cause: Throwable) : RuntimeException("Network Error", cause)

// Sunucu 4xx, 5xx
class ApiException(
    val code: Int,
    val errorMessage: String?,
    val errorBody: ErrorResponseDto? = null,
    cause: Throwable? = null
) : RuntimeException("HTTP $code: $errorMessage", cause)
