package com.cosmocats.marketplace.integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.math.BigDecimal

@Service
class IntergalacticCurrencyClient(
    private val restClient: RestClient,
    @Value("\${currency.api.base-url:https://api.frankfurter.app}") private val baseUrl: String
) {
    fun euroRateFromUsd(): BigDecimal {
        val response = restClient.get()
            .uri("$baseUrl/latest?from=USD&to=EUR")
            .retrieve()
            .body(CurrencyRateResponse::class.java)
        return response?.rates?.get("EUR") ?: BigDecimal.ONE
    }
}

data class CurrencyRateResponse(
    val rates: Map<String, BigDecimal> = emptyMap()
)
