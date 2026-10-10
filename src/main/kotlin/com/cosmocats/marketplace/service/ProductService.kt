package com.cosmocats.marketplace.service

import com.cosmocats.marketplace.api.dto.PagedResponse
import com.cosmocats.marketplace.api.dto.ProductCreateRequest
import com.cosmocats.marketplace.api.dto.ProductResponse
import com.cosmocats.marketplace.api.dto.ProductUpdateRequest
import java.util.UUID

interface ProductService {
    fun create(request: ProductCreateRequest): ProductResponse
    fun getById(id: UUID): ProductResponse
    fun getAll(page: Int, size: Int): PagedResponse<ProductResponse>
    fun update(id: UUID, request: ProductUpdateRequest): ProductResponse
    fun delete(id: UUID)
}
