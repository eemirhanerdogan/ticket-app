package com.example.core.domain

data class Ticket(
    val id: String,
    val purchaseId: String?,
    val ticketTypeId: String,
    val qrCode: String,
    val status: String,
    val usedAt: String?,
    val checkedInBy: String?,
    val ticketTypeName: String?,
    val priceCents: Int?,
    val eventId: String?,
    val eventName: String?,
    val eventStartsAt: String?,
    val eventPlace: String?
)
