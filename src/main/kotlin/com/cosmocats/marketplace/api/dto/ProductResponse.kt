package com.cosmocats.marketplace.api.dto

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val category: CategoryResponse,
    val stock: Int,
    val createdAt: Instant
)

data class CategoryResponse(
    val id: UUID,
    val name: String,
    val code: String
)
