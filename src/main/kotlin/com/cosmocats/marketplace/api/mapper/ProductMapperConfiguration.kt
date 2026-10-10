package com.cosmocats.marketplace.api.mapper

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductMapperConfiguration {
    @Bean
    fun productMapper(): ProductMapper = ProductMapper()
}
