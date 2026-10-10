package com.cosmocats.marketplace.api.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CosmicWordValidator : ConstraintValidator<CosmicWordCheck, String?> {
    private val cosmicWords = setOf("star", "galaxy", "comet", "nebula", "cosmo", "orbit")

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean =
        value != null && cosmicWords.any(value.lowercase()::contains)
}
