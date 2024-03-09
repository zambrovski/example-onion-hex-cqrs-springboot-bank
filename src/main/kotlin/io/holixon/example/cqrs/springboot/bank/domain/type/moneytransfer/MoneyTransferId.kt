package io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer

import org.jmolecules.ddd.annotation.ValueObject

/**
 * Represents a money transfer id.
 */
@JvmInline
@ValueObject
value class MoneyTransferId private constructor(val value: String) {
  companion object {
    fun of(value: String): MoneyTransferId {
      require(value.isNotBlank()) { "Money transfer id must not be empty" }
      return MoneyTransferId(value)
    }
  }
}
