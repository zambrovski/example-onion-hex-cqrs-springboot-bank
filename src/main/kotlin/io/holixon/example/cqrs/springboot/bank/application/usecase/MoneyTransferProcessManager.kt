package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.out.MoneyTransferIdGenerator
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferAggregateRepository
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CancelMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CompleteMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.ReceiveMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.model.moneytransfer.MoneyTransferAggregate
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.*
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.MaximumBalanceExceeded
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import mu.KLogging
import org.jmolecules.architecture.cqrs.CommandDispatcher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.transaction.annotation.Transactional

open class MoneyTransferProcessManager(
  private val moneyTransferIdGenerator: MoneyTransferIdGenerator,
  private val bankAccountRepository: BankAccountAggregateRepository,
  private val moneyTransferAggregateRepository: MoneyTransferAggregateRepository,
  private val applicationEventPublisher: ApplicationEventPublisher,
) {

  companion object : KLogging()

  fun execute(sourceAccountId: AccountId, targetAccountId: AccountId, amount: Amount): MoneyTransferId {
    val moneyTransferId = moneyTransferIdGenerator.get()

    logger.info { "Execute $moneyTransferId" }
    val moneyTransferAggregate = moneyTransferAggregateRepository.save(
      MoneyTransferAggregate.init(
        moneyTransferId = moneyTransferId,
        sourceAccountId = sourceAccountId,
        targetAccountId = targetAccountId,
        amount = amount
      )
    )
    val sourceBankAccount = bankAccountRepository.load(sourceAccountId)

    moneyTransferAggregate
      .request(sourceBankAccount)
      .onFailure { e ->
        applicationEventPublisher.publishEvent(
          MoneyTransferFailedEvent(
            moneyTransferId = moneyTransferId,
            sourceAccountId = sourceAccountId,
            targetAccountId = targetAccountId,
            amount = amount,
            reason = RejectionReason.of(e.message)
          )
        )
      }
      .onSuccess { event ->
        bankAccountRepository.save(sourceBankAccount)
        applicationEventPublisher.publishEvent(event)
      }

    return moneyTransferId
  }

  @Async
  @EventListener
  open fun requested(evt: MoneyTransferRequestedEvent) {
    val moneyTransferAggregate = moneyTransferAggregateRepository.load(evt.moneyTransferId)
    val targetBankAccount = bankAccountRepository.load(evt.targetAccountId)
    logger.info { "Requested ${evt.moneyTransferId}" }
    moneyTransferAggregate
      .receive(targetBankAccount)
      .onFailure { e ->
        logger.info { "Failed by target ${evt.moneyTransferId}" }
        val reason = RejectionReason.of(e.message)
        // compensate
        val sourceBankAccount = bankAccountRepository.load(evt.sourceAccountId)
        val cancellationResult = moneyTransferAggregate.cancel(sourceBankAccount, reason)
        bankAccountRepository.save(sourceBankAccount)
        applicationEventPublisher.publishEvent(
          cancellationResult
        )
      }
      .onSuccess { event ->
        bankAccountRepository.save(targetBankAccount)
        applicationEventPublisher.publishEvent(event)
      }
  }

  @Async
  @EventListener
  open fun received(evt: MoneyTransferReceivedEvent) {
    val moneyTransferAggregate = moneyTransferAggregateRepository.load(evt.moneyTransferId)
    val sourceBankAccount = bankAccountRepository.load(evt.sourceAccountId)
    logger.info { "Received ${evt.moneyTransferId}" }
    moneyTransferAggregate
      .complete(sourceBankAccount)
      .onFailure {
        logger.info { "Failed on complete ${evt.moneyTransferId}" }
        applicationEventPublisher.publishEvent(
          MoneyTransferCancelledEvent(
            moneyTransferId = evt.moneyTransferId,
            reason = RejectionReason.of("Unknown error, could not complete money transfer.")
          )
        )
      }
      .onSuccess { event ->
        logger.info { "Completed ${evt.moneyTransferId}" }
        bankAccountRepository.save(sourceBankAccount)
        applicationEventPublisher.publishEvent(event)
        applicationEventPublisher.publishEvent(
          MoneyTransferredEvent(
            moneyTransferId = evt.moneyTransferId,
            sourceAccountId = evt.sourceAccountId,
            targetAccountId = evt.targetAccountId,
            amount = evt.amount
          )
        )
      }
  }

}
