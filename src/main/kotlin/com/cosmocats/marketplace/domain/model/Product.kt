package com.cosmocats.marketplace.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Product(
    val id: UUID,
    var name: String,
    var description: String,
    var price: BigDecimal,
    var category: Category,
    var stock: Int,
    val createdAt: Instant
)
