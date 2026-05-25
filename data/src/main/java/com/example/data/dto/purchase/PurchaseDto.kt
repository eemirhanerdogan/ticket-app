package com.example.data.dto.purchase

import com.example.data.dto.TicketDto
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseDto(
    val id: String,
    val status: String? = null,
    val totalCents: Int? = null,
    val paidAt: String? = null,
    val items: List<PurchaseItemDto> = emptyList(),
    val tickets: List<TicketDto> = emptyList()
)
