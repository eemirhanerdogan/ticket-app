package com.example.data.repository

import com.example.core.domain.checkin.CheckInRepository
import com.example.core.domain.checkin.CheckInResult
import com.example.data.dto.checkin.CheckInRequestDto
import com.example.data.mapper.toDomain
import com.example.data.remote.CheckInApi
import com.example.data.util.runCatchingApi

class CheckInRepositoryImpl(
    private val checkInApi: CheckInApi
) : CheckInRepository {
    override suspend fun scan(qrCode: String): Result<CheckInResult> = runCatchingApi {
        checkInApi.scan(CheckInRequestDto(qrCode = qrCode)).toDomain()
    }
}
