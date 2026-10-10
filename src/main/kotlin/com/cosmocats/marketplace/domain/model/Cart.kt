package com.cosmocats.marketplace.domain.model

import java.util.UUID

data class Cart(
    val id: UUID = UUID.randomUUID(),
    val productIds: MutableList<UUID> = mutableListOf()
)
