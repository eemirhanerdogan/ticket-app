package com.example.data.remote

import com.example.data.dto.TicketDto
import com.example.data.dto.event.EventDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketApi {
    @GET("events")
    suspend fun getEvents(@Query("upcoming") upcoming: Boolean = true): List<EventDto>

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: String): EventDto

    @GET("me/tickets")
    suspend fun getMyTickets(): List<TicketDto>

    @GET("me/tickets/{id}")
    suspend fun getTicketDetail(@Path("id") id: String): TicketDto
}
