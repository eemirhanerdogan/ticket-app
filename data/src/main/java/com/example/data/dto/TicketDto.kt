package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TicketDto(
    val id: String,
    val qrCode: String? = null,
    val status: String? = null,
    val ticketTypeId: String? = null,
    val event: EventDto? = null,
    val purchaseDate: String? = null
)
