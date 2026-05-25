package com.example.data.dto.purchase

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseItemDto(
    val id: String? = null,
    val ticketTypeId: String? = null,
    val quantity: Int? = null,
    val unitPriceCents: Int? = null
)
