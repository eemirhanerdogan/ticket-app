package com.example.data.repository

import com.example.core.domain.Event
import com.example.core.domain.Ticket
import com.example.core.domain.TicketRepository
import com.example.data.remote.TicketApi
import com.example.data.util.runCatchingApi

class TicketRepositoryImpl(
    private val ticketApi: TicketApi
) : TicketRepository {
    override suspend fun getMyTickets(): Result<List<Ticket>> = runCatchingApi {
        ticketApi.getMyTickets().map { dto ->
            val eventDto = requireNotNull(dto.event) { "Event is missing in ticket ${dto.id}" }
            Ticket(
                id = dto.id,
                event = Event(
                    id = eventDto.id,
                    title = eventDto.title,
                    date = eventDto.date,
                    location = eventDto.location,
                    price = eventDto.price,
                    availableTickets = eventDto.availableTickets,
                    imageUrl = eventDto.imageUrl
                ),
                purchaseDate = dto.purchaseDate
            )
        }
    }

    override suspend fun getTicketDetail(id: String): Result<Ticket> = runCatchingApi {
        val dto = ticketApi.getTicketDetail(id)
        val eventDto = requireNotNull(dto.event) { "Event is missing in ticket ${dto.id}" }
        Ticket(
            id = dto.id,
            event = Event(
                id = eventDto.id,
                title = eventDto.title,
                date = eventDto.date,
                location = eventDto.location,
                price = eventDto.price,
                availableTickets = eventDto.availableTickets,
                imageUrl = eventDto.imageUrl
            ),
            purchaseDate = dto.purchaseDate
        )
    }
}
