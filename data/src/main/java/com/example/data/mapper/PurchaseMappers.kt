package com.example.data.mapper

import com.example.core.domain.purchase.Purchase
import com.example.core.domain.purchase.PurchaseItem
import com.example.core.domain.purchase.PurchaseStatus
import com.example.data.dto.purchase.PurchaseDto
import com.example.data.dto.purchase.PurchaseItemDto

fun PurchaseDto.toDomain(): Purchase {
    return Purchase(
        id = id,
        status = when (status?.uppercase()) {
            "PENDING" -> PurchaseStatus.PENDING
            "PAID" -> PurchaseStatus.PAID
            else -> PurchaseStatus.UNKNOWN
        },
        totalCents = totalCents ?: 0,
        paidAt = paidAt,
        items = items.map { it.toDomain() },
        tickets = tickets.map { it.toDomain() }
    )
}

fun PurchaseItemDto.toDomain(): PurchaseItem {
    return PurchaseItem(
        id = id.orEmpty(),
        ticketTypeId = ticketTypeId.orEmpty(),
        quantity = quantity ?: 0,
        unitPriceCents = unitPriceCents ?: 0
    )
}
