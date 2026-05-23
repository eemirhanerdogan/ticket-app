package com.example.data.mapper

import com.example.core.domain.event.Event
import com.example.core.domain.event.TicketType
import com.example.data.dto.event.EventDto
import com.example.data.dto.event.TicketTypeDto

internal fun EventDto.toDomain(): Event = Event(
    id = id,
    name = name,
    description = description,
    venue = place.orEmpty(),
    startsAt = startsAt,
    endsAt = endsAt.orEmpty(),
    ticketTypes = ticketTypes.map { it.toDomain() }
)

internal fun TicketTypeDto.toDomain(): TicketType = TicketType(
    id = id,
    name = name,
    priceCents = priceCents,
    capacity = capacity,
    soldCount = soldCount,
    remaining = remaining
)
