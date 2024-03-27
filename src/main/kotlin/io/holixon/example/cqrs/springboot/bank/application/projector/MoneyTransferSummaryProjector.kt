package io.holixon.example.cqrs.springboot.bank.application.projector

import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCancelledEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCompletedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferFailedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferRequestedEvent
import io.holixon.example.cqrs.springboot.bank.domain.query.model.BankAccountMoneyTransfer
import org.jmolecules.event.annotation.DomainEventHandler
import org.springframework.context.event.EventListener

class MoneyTransferSummaryProjector(
  private val repository: MoneyTransferSummaryRepository
) {

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferRequestedEvent")
  @EventListener
  fun on(evt: MoneyTransferRequestedEvent) {
    repository.save(
      BankAccountMoneyTransfer(
        moneyTransferId = evt.moneyTransferId,
        sourceAccountId = evt.sourceAccountId,
        targetAccountId = evt.targetAccountId,
        amount = evt.amount,
        success = false
      )
    )
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferCompletedEvent")
  @EventListener
  fun on(evt: MoneyTransferCompletedEvent) {
    repository.findById(evt.moneyTransferId).ifPresent { found ->
      repository.save(found.complete())
    }
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferCancelledEvent")
  @EventListener
  fun on(evt: MoneyTransferCancelledEvent) {
    repository.findById(evt.moneyTransferId).ifPresent { found ->
      repository.save(found.cancel(evt.reason))
    }
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferFailedEvent")
  @EventListener
  fun on(evt: MoneyTransferFailedEvent) {
    repository.save(
      BankAccountMoneyTransfer(
        moneyTransferId = evt.moneyTransferId,
        sourceAccountId = evt.sourceAccountId,
        targetAccountId = evt.targetAccountId,
        amount = evt.amount,
        success = false
      ).cancel(
        rejectionReason = evt.reason
      )
    )
  }

}
