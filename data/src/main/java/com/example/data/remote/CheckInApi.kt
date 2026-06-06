package com.example.data.remote

import com.example.data.dto.checkin.CheckInRequestDto
import com.example.data.dto.checkin.CheckInResultDto
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckInApi {
    @POST("/checkin/scan")
    suspend fun scan(@Body body: CheckInRequestDto): CheckInResultDto
}
