package io.holixon.example.cqrs.springboot.bank.domain.type.account

import org.jmolecules.ddd.annotation.ValueObject

/**
 * Represents amount.
 */
@JvmInline
@ValueObject
value class Amount private constructor(val value: Int) {
  companion object {
    fun of(value: Int): Amount {
      require(value > 0) { "Amount must be positive, but it was $value." }
      return Amount(value)
    }
  }

  override fun toString(): String = "'$value'"
}

