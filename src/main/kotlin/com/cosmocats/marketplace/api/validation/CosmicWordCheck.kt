package com.cosmocats.marketplace.api.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [CosmicWordValidator::class])
annotation class CosmicWordCheck(
    val message: String = "Name must contain a cosmic word: star, galaxy, comet, nebula, cosmo, or orbit",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
