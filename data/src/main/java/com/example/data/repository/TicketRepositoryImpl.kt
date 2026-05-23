package com.example.data.repository

import com.example.core.domain.Ticket
import com.example.core.domain.TicketRepository
import com.example.data.mapper.toDomain
import com.example.data.remote.TicketApi
import com.example.data.util.runCatchingApi

class TicketRepositoryImpl(
    private val ticketApi: TicketApi
) : TicketRepository {
    override suspend fun getMyTickets(): Result<List<Ticket>> = runCatchingApi {
        ticketApi.getMyTickets().map { it.toDomain() }
    }

    override suspend fun getTicketDetail(id: String): Result<Ticket> = runCatchingApi {
        ticketApi.getTicketDetail(id).toDomain()
    }
}
