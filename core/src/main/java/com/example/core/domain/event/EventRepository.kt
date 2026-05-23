package com.example.core.domain.event

interface EventRepository {
    suspend fun getEvents(): Result<List<Event>>
}
