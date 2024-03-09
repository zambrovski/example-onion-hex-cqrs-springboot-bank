package io.holixon.example.cqrs.springboot.bank.infrastructure

import org.jmolecules.architecture.onion.classical.InfrastructureRing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

/**
 * Starts the application.
 */
fun main(args: Array<String>) {
  runApplication<HexagonalSpringBootCQRSHoliBankApplication>(*args).let { }
}

@InfrastructureRing
@SpringBootApplication
@Import(
  BankContextConfiguration::class,
  InfrastructureConfiguration::class
)
class HexagonalSpringBootCQRSHoliBankApplication
