package com.example.core.domain.checkin

interface CheckInRepository {
    suspend fun scan(qrCode: String): Result<CheckInResult>
}
