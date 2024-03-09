package io.holixon.example.cqrs.springboot.bank.application.projector

import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.domain.event.BankAccountCreatedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.MoneyDepositedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.MoneyWithdrawnEvent
import io.holixon.example.cqrs.springboot.bank.domain.query.BankAccountCurrentBalance
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferredEvent
import org.jmolecules.event.annotation.DomainEventHandler
import org.springframework.context.event.EventListener

class BankAccountCurrentBalanceProjector(
  private val repository: BankAccountCurrentBalanceRepository
) {

  @DomainEventHandler(namespace = "springboot.bank", name = "BankAccountCreatedEvent")
  @EventListener
  fun on(evt: BankAccountCreatedEvent) {
    repository.save(BankAccountCurrentBalance(accountId = evt.accountId, balance = evt.initialBalance))
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyWithdrawnEvent")
  @EventListener
  fun on(evt: MoneyWithdrawnEvent) {
    repository.findById(evt.accountId).ifPresent { found ->
      repository.save(found.decreaseBalance(evt.amount))
    }
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyDepositedEvent")
  @EventListener
  fun on(evt: MoneyDepositedEvent) {
    repository.findById(evt.accountId).ifPresent { found ->
      repository.save(found.increaseBalance(evt.amount))
    }
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferredEvent")
  @EventListener
  fun on(evt: MoneyTransferredEvent) {
    repository.findById(evt.sourceAccountId).ifPresent { found ->
      repository.save(found.decreaseBalance(evt.amount))
    }
    repository.findById(evt.targetAccountId).ifPresent { found ->
      repository.save(found.increaseBalance(evt.amount))
    }
  }
}
