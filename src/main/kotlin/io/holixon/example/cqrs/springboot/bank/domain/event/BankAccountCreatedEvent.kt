package io.holixon.example.cqrs.springboot.bank.domain.event

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Balance
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "BankAccountCreatedEvent")
data class BankAccountCreatedEvent(
  val accountId: AccountId,
  val initialBalance: Balance
)
