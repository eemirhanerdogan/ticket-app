package com.example.core.domain.checkin

data class CheckInResult(
    val ticketId: String,
    val ticketTypeName: String,
    val eventName: String,
    val eventPlace: String,
    val eventStartsAt: String?,
    val eventEndsAt: String?,
    val checkedInAt: String?
)
