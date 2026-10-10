package com.cosmocats.marketplace.api.mapper

import com.cosmocats.marketplace.api.dto.CategoryResponse
import com.cosmocats.marketplace.api.dto.ProductCreateRequest
import com.cosmocats.marketplace.api.dto.ProductResponse
import com.cosmocats.marketplace.domain.model.Category
import com.cosmocats.marketplace.domain.model.Product
import java.time.Instant
import java.util.UUID

class ProductMapper {
    fun toDto(domain: Product): ProductResponse = ProductResponse(
        id = domain.id,
        name = domain.name,
        description = domain.description,
        price = domain.price,
        category = CategoryResponse(domain.category.id, domain.category.name, domain.category.code),
        stock = domain.stock,
        createdAt = domain.createdAt
    )

    fun toDomain(dto: ProductCreateRequest, category: Category): Product = Product(
        id = UUID.randomUUID(),
        name = dto.name,
        description = dto.description,
        price = dto.price,
        category = category,
        stock = dto.stock,
        createdAt = Instant.now()
    )
}

// Bonus alternative when MapStruct is enabled:
// @Mapper(componentModel = "spring")
// interface MapStructProductMapper {
//     fun toDto(domain: Product): ProductResponse
//     fun toDomain(dto: ProductCreateRequest): Product
// }
