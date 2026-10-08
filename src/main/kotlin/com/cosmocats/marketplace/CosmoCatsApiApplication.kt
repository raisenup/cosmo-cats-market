package com.cosmocats.marketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CosmoCatsApiApplication

fun main(args: Array<String>) {
	runApplication<CosmoCatsApiApplication>(*args)
}
