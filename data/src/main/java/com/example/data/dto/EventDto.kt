package com.example.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val price: Double,
    val availableTickets: Int,
    val imageUrl: String? = null
)
