package io.holixon.example.cqrs.springboot.bank.domain.event

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "MoneyDepositedEvent")
data class MoneyDepositedEvent(
  val accountId: AccountId,
  val amount: Amount
)
