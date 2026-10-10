package com.cosmocats.marketplace.domain.model

import java.time.Instant
import java.util.UUID

data class Order(
    val id: UUID = UUID.randomUUID(),
    val productIds: List<UUID> = emptyList(),
    val createdAt: Instant = Instant.now()
)
