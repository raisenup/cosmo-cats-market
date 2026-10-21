package com.cosmocats.marketplace.service

import com.cosmocats.marketplace.api.dto.ProductCreateRequest
import com.cosmocats.marketplace.api.dto.ProductUpdateRequest
import com.cosmocats.marketplace.api.mapper.ProductMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class InMemoryProductServiceTest {
    private val service = InMemoryProductService(ProductMapper())
    private val yarnCategory = UUID.fromString("00000000-0000-0000-0000-000000000001")
    private val treatsCategory = UUID.fromString("00000000-0000-0000-0000-000000000002")

    @Test
    fun `create stores and returns a product`() {
        val response = service.create(createRequest())

        assertEquals("Galaxy cat collar", response.name)
        assertEquals(yarnCategory, response.category.id)
        assertEquals(response, service.getById(response.id))
    }

    @Test
    fun `getById throws for an unknown product`() {
        assertThrows(ProductNotFoundException::class.java) { service.getById(UUID.randomUUID()) }
    }

    @Test
    fun `getAll validates pagination and returns requested page`() {
        val firstPage = service.getAll(page = 0, size = 1)

        assertEquals(1, firstPage.content.size)
        assertEquals(2, firstPage.totalElements)
        assertEquals(2, firstPage.totalPages)
        assertThrows(IllegalArgumentException::class.java) { service.getAll(-1, 1) }
        assertThrows(IllegalArgumentException::class.java) { service.getAll(0, 0) }
    }

    @Test
    fun `update changes all mutable product fields`() {
        val product = service.create(createRequest())
        val request = ProductUpdateRequest(
            name = "Comet cat collar",
            description = "A durable collar made for comet chasers.",
            price = BigDecimal("19.99"),
            categoryId = treatsCategory,
            stock = 7
        )

        val updated = service.update(product.id, request)

        assertEquals(request.name, updated.name)
        assertEquals(request.description, updated.description)
        assertEquals(request.price, updated.price)
        assertEquals(request.categoryId, updated.category.id)
        assertEquals(request.stock, updated.stock)
    }

    @Test
    fun `delete removes an existing product and rejects a second delete`() {
        val product = service.create(createRequest())

        service.delete(product.id)

        assertThrows(ProductNotFoundException::class.java) { service.getById(product.id) }
        assertThrows(ProductNotFoundException::class.java) { service.delete(product.id) }
    }

    @Test
    fun `create rejects an unknown category`() {
        assertThrows(NoSuchElementException::class.java) {
            service.create(createRequest(categoryId = UUID.randomUUID()))
        }
    }

    private fun createRequest(categoryId: UUID = yarnCategory) = ProductCreateRequest(
        name = "Galaxy cat collar",
        description = "A durable collar for intergalactic cats.",
        price = BigDecimal("14.50"),
        categoryId = categoryId,
        stock = 12
    )
}