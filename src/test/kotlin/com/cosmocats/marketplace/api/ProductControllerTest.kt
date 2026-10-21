package com.cosmocats.marketplace.api

import com.cosmocats.marketplace.api.dto.CategoryResponse
import com.cosmocats.marketplace.api.dto.PagedResponse
import com.cosmocats.marketplace.api.dto.ProductCreateRequest
import com.cosmocats.marketplace.api.dto.ProductResponse
import com.cosmocats.marketplace.api.dto.ProductUpdateRequest
import com.cosmocats.marketplace.service.ProductService
import jakarta.validation.Validation
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class ProductControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var service: RecordingProductService

    @BeforeEach
    fun setUp() {
        service = RecordingProductService()
        val validator = Validation.buildDefaultValidatorFactory().validator
        mockMvc = MockMvcBuilders.standaloneSetup(ProductController(service))
            .setControllerAdvice(GlobalExceptionHandler())
            .setValidator(org.springframework.validation.beanvalidation.SpringValidatorAdapter(validator))
            .build()
    }

    @Test
    fun `valid create returns 201 and delegates to service`() {
        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson())
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Galaxy cat collar"))

        assertTrue(service.created)
    }

    @Test
    fun `invalid create returns validation problem`() {
        mockMvc.perform(
            post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson().replace("Galaxy cat collar", "ordinary collar"))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.title").value("Validation failed"))
    }

    @Test
    fun `malformed product id returns validation problem`() {
        mockMvc.perform(get("/api/v1/products/not-a-uuid"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
    }

    @Test
    fun `valid update and delete return expected statuses`() {
        val id = service.response.id
        mockMvc.perform(
            put("/api/v1/products/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson().replace("Galaxy cat collar", "Comet cat collar"))
        ).andExpect(status().isOk)

        mockMvc.perform(delete("/api/v1/products/$id"))
            .andExpect(status().isNoContent)
    }

    private fun validJson() = """
        {
          "name": "Galaxy cat collar",
          "description": "A durable collar for intergalactic cats.",
          "price": 14.50,
          "categoryId": "00000000-0000-0000-0000-000000000001",
          "stock": 12
        }
    """.trimIndent()

    private class RecordingProductService : ProductService {
        val response = ProductResponse(
            UUID.randomUUID(), "Galaxy cat collar", "A durable collar for intergalactic cats.",
            BigDecimal("14.50"),
            CategoryResponse(
                UUID.fromString("00000000-0000-0000-0000-000000000001"), "Cosmic yarns", "YARN"
            ), 12, Instant.now()
        )
        var created = false

        override fun create(request: ProductCreateRequest): ProductResponse {
            created = true
            return response
        }

        override fun getById(id: UUID) = response
        override fun getAll(page: Int, size: Int) = PagedResponse(listOf(response), page, size, 1, 1)
        override fun update(id: UUID, request: ProductUpdateRequest) = response
        override fun delete(id: UUID) = Unit
    }
}