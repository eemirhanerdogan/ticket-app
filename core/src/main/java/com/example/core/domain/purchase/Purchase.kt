package com.example.core.domain.purchase

import com.example.core.domain.Ticket

data class Purchase(
    val id: String,
    val status: PurchaseStatus,
    val totalCents: Int,
    val paidAt: String?,
    val items: List<PurchaseItem>,
    val tickets: List<Ticket>
)
