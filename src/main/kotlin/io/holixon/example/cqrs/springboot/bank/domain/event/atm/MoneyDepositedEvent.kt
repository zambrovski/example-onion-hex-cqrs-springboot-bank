package io.holixon.example.cqrs.springboot.bank.domain.event.atm

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "MoneyDepositedEvent")
data class MoneyDepositedEvent(
  val accountId: AccountId,
  val amount: Amount
)
