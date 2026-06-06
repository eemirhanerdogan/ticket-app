package com.example.data.mapper

import com.example.core.domain.checkin.CheckInResult
import com.example.data.dto.checkin.CheckInResultDto

fun CheckInResultDto.toDomain(): CheckInResult {
    return CheckInResult(
        ticketId = ticketId.orEmpty(),
        ticketTypeName = ticketType?.name ?: "Bilinmeyen Bilet Türü",
        eventName = ticketType?.event?.name ?: "Bilinmeyen Etkinlik",
        eventPlace = ticketType?.event?.place ?: "Lokasyon bilgisi yok",
        eventStartsAt = ticketType?.event?.startsAt,
        eventEndsAt = ticketType?.event?.endsAt,
        checkedInAt = checkedInAt
    )
}
