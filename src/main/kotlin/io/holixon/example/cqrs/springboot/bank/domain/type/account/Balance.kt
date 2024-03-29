package io.holixon.example.cqrs.springboot.bank.domain.type.account

import org.jmolecules.ddd.annotation.ValueObject

/**
 * Represents balance.
 */
@JvmInline
@ValueObject
value class Balance private constructor(val value: Int) {

  companion object {
    fun of(value: Int): Balance = Balance(value)
  }

  operator fun plus(other: Balance): Balance = Balance(this.value + other.value)
  operator fun plus(value: Amount): Balance = Balance(this.value + value.value)
  operator fun minus(value: Amount): Balance = Balance(this.value - value.value)
  operator fun minus(value: ReservedAmount): Balance = Balance(this.value - value.value)
  operator fun compareTo(other: Balance): Int = this.value.compareTo(other.value)

  override fun toString(): String = "'$value'"
}
