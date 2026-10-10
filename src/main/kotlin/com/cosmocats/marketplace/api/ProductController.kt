package com.cosmocats.marketplace.api

import com.cosmocats.marketplace.api.dto.PagedResponse
import com.cosmocats.marketplace.api.dto.ProductCreateRequest
import com.cosmocats.marketplace.api.dto.ProductResponse
import com.cosmocats.marketplace.api.dto.ProductUpdateRequest
import com.cosmocats.marketplace.service.ProductService
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Positive
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/v1/products")
@Validated
class ProductController(private val productService: ProductService) {
    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "0") @Min(0) page: Int,
        @RequestParam(defaultValue = "20") @Positive @Max(100) size: Int
    ): PagedResponse<ProductResponse> = productService.getAll(page, size)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ProductResponse = productService.getById(id)

    @PostMapping
    fun create(@Valid @RequestBody request: ProductCreateRequest): ResponseEntity<ProductResponse> {
        val response = productService.create(request)
        return ResponseEntity.created(URI.create("/api/v1/products/${response.id}")).body(response)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: ProductUpdateRequest): ProductResponse =
        productService.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        productService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
