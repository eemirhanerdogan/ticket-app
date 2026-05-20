package com.example.core.domain

data class Ticket(
    val id: String,
    val event: Event,
    val purchaseDate: String? = null
)
