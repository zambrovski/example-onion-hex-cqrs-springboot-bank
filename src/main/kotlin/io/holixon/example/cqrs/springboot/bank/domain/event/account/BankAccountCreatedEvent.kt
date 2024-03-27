package io.holixon.example.cqrs.springboot.bank.domain.event.account

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Balance
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "BankAccountCreatedEvent")
data class BankAccountCreatedEvent(
  val accountId: AccountId,
  val initialBalance: Balance
)
