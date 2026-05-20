package com.example.core.domain

interface TicketRepository {
    suspend fun getMyTickets(): Result<List<Ticket>>
    suspend fun getTicketDetail(id: String): Result<Ticket>
}
