package com.cosmocats.marketplace.service

import java.util.UUID

class ProductNotFoundException(id: UUID) : NoSuchElementException("Product '$id' was not found")
