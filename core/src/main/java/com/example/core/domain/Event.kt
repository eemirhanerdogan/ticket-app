package com.example.core.domain

data class Event(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val price: Double,
    val availableTickets: Int,
    val imageUrl: String? = null
)
