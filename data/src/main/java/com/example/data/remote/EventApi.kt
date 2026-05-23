package com.example.data.remote

import com.example.data.dto.event.EventDto
import retrofit2.http.GET

interface EventApi {
    @GET("events")
    suspend fun getEvents(): List<EventDto>
}
