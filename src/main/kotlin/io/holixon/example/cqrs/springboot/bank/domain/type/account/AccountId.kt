package io.holixon.example.cqrs.springboot.bank.domain.type.account

import org.jmolecules.ddd.annotation.ValueObject

/**
 * Represents account id.
 */
@JvmInline
@ValueObject
value class AccountId private constructor(val value: String) {
  companion object {
    fun of(value: String): AccountId {
      require(value.isNotBlank()) { "Account id must not be empty." }
      return AccountId(value)
    }
  }

  override fun toString(): String = "'$value'"
}
