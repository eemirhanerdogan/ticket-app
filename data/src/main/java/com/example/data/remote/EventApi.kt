package com.example.data.remote

import com.example.data.dto.event.EventDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EventApi {
    @GET("events")
    suspend fun getEvents(): List<EventDto>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventDto
}
