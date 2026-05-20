package com.example.data.repository

import com.example.core.domain.Event
import com.example.core.domain.EventRepository
import com.example.core.domain.UserRole
import com.example.data.remote.TicketApi
import com.example.data.util.runCatchingApi

class EventRepositoryImpl(
    private val ticketApi: TicketApi
) : EventRepository {
    override suspend fun getEvents(): Result<List<Event>> = runCatchingApi {
        ticketApi.getEvents().map { dto ->
            Event(
                id = dto.id,
                title = dto.title,
                date = dto.date,
                location = dto.location,
                price = dto.price,
                availableTickets = dto.availableTickets,
                imageUrl = dto.imageUrl
            )
        }
    }

    override suspend fun getEventDetail(id: String): Result<Event> = runCatchingApi {
        val dto = ticketApi.getEventDetail(id)
        Event(
            id = dto.id,
            title = dto.title,
            date = dto.date,
            location = dto.location,
            price = dto.price,
            availableTickets = dto.availableTickets,
            imageUrl = dto.imageUrl
        )
    }
}
