package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TicketDto(
    val id: String,
    val purchaseId: String? = null,
    val ticketTypeId: String? = null,
    val qrCode: String? = null,
    val status: String? = null,
    val usedAt: String? = null,
    val checkedInBy: String? = null,
    val ticketType: TicketTypeDto? = null
)

@Serializable
data class TicketTypeDto(
    val id: String? = null,
    val eventId: String? = null,
    val name: String? = null,
    val priceCents: Int? = null,
    val capacity: Int? = null,
    val soldCount: Int? = null,
    val event: TicketEventDto? = null
)

@Serializable
data class TicketEventDto(
    val id: String? = null,
    val name: String? = null,
    val startsAt: String? = null,
    val place: String? = null
)
