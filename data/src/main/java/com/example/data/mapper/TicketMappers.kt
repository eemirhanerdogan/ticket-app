package com.example.data.mapper

import com.example.core.domain.Ticket
import com.example.data.dto.TicketDto

fun TicketDto.toDomain(): Ticket {
    return Ticket(
        id = id,
        purchaseId = purchaseId,
        ticketTypeId = ticketTypeId.orEmpty(),
        qrCode = qrCode ?: "QR kod bulunamadı",
        status = status ?: "UNKNOWN",
        usedAt = usedAt,
        checkedInBy = checkedInBy,
        ticketTypeName = ticketType?.name,
        priceCents = ticketType?.priceCents,
        eventId = ticketType?.event?.id ?: ticketType?.eventId,
        eventName = ticketType?.event?.name,
        eventStartsAt = ticketType?.event?.startsAt,
        eventPlace = ticketType?.event?.place
    )
}
