package com.example.data.dto.checkin

import kotlinx.serialization.Serializable

@Serializable
data class CheckInRequestDto(
    val qrCode: String
)
