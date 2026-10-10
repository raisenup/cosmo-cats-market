package com.cosmocats.marketplace.service

import com.cosmocats.marketplace.api.dto.PagedResponse
import com.cosmocats.marketplace.api.dto.ProductCreateRequest
import com.cosmocats.marketplace.api.dto.ProductResponse
import com.cosmocats.marketplace.api.dto.ProductUpdateRequest
import com.cosmocats.marketplace.api.mapper.ProductMapper
import com.cosmocats.marketplace.domain.model.Category
import com.cosmocats.marketplace.domain.model.Product
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class InMemoryProductService(
    private val mapper: ProductMapper
) : ProductService {
    private val products = ConcurrentHashMap<UUID, Product>()
    private val categories = listOf(
        Category(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Cosmic yarns", "YARN"),
        Category(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Galactic treats", "TREAT")
    )

    init {
        val yarn = categories[0]
        val treats = categories[1]
        listOf(
            Product(UUID.randomUUID(), "Anti-gravity star yarn", "A weightless yarn spun from stellar silk.", BigDecimal("42.50"), yarn, 18, java.time.Instant.now()),
            Product(UUID.randomUUID(), "Cosmic comet milk", "Creamy comet milk for intergalactic kittens.", BigDecimal("12.75"), treats, 32, java.time.Instant.now())
        ).forEach { products[it.id] = it }
    }

    override fun create(request: ProductCreateRequest): ProductResponse {
        val product = mapper.toDomain(request, category(request.categoryId))
        products[product.id] = product
        return mapper.toDto(product)
    }

    override fun getById(id: UUID): ProductResponse = mapper.toDto(find(id))

    override fun getAll(page: Int, size: Int): PagedResponse<ProductResponse> {
        require(page >= 0) { "Page must be greater than or equal to 0" }
        require(size > 0) { "Size must be greater than 0" }
        val sorted = products.values.sortedBy { it.createdAt }
        val from = (page.toLong() * size).coerceAtMost(sorted.size.toLong()).toInt()
        val to = (from + size).coerceAtMost(sorted.size)
        val totalPages = if (sorted.isEmpty()) 0 else (sorted.size + size - 1) / size
        return PagedResponse(sorted.subList(from, to).map(mapper::toDto), page, size, sorted.size.toLong(), totalPages)
    }

    override fun update(id: UUID, request: ProductUpdateRequest): ProductResponse {
        val product = find(id)
        product.name = request.name
        product.description = request.description
        product.price = request.price
        product.category = category(request.categoryId)
        product.stock = request.stock
        return mapper.toDto(product)
    }

    override fun delete(id: UUID) {
        if (products.remove(id) == null) throw ProductNotFoundException(id)
    }

    private fun find(id: UUID): Product = products[id] ?: throw ProductNotFoundException(id)

    private fun category(id: UUID): Category = categories.firstOrNull { it.id == id }
        ?: throw NoSuchElementException("Category '$id' was not found")
}
