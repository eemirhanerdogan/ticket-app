package com.example.data.dto.checkin

import kotlinx.serialization.Serializable

@Serializable
data class CheckInResultDto(
    val ticketId: String? = null,
    val ticketType: CheckInTicketTypeDto? = null,
    val checkedInAt: String? = null
)

@Serializable
data class CheckInTicketTypeDto(
    val id: String? = null,
    val event: CheckInEventDto? = null,
    val name: String? = null,
    val priceCents: Int? = null,
    val capacity: Int? = null,
    val soldCount: Int? = null,
    val remaining: Int? = null
)

@Serializable
data class CheckInEventDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val place: String? = null,
    val startsAt: String? = null,
    val endsAt: String? = null
)
