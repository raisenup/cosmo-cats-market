package com.cosmocats.marketplace.api.dto

import com.cosmocats.marketplace.api.validation.CosmicWordCheck
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.UUID

 data class ProductCreateRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 120)
    @field:CosmicWordCheck
    val name: String,
    @field:NotBlank
    @field:Size(min = 10, max = 1000)
    val description: String,
    @field:NotNull
    @field:DecimalMin(value = "0.01")
    @field:Positive
    val price: BigDecimal,
    @field:NotNull
    val categoryId: UUID,
    @field:NotNull
    @field:Positive
    val stock: Int
)
