package io.holixon.example.cqrs.springboot.bank.domain.command.model

import io.holixon.example.cqrs.springboot.bank.domain.command.api.account.CreateBankAccountCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.atm.DepositMoneyCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.atm.WithdrawMoneyCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CancelMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CompleteMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.ReceiveMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.RequestMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.model.account.BankAccount
import io.holixon.example.cqrs.springboot.bank.domain.event.account.BankAccountCreatedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.atm.MoneyDepositedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.atm.MoneyWithdrawnEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCancelledEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCompletedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferReceivedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferRequestedEvent
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import org.jmolecules.architecture.cqrs.CommandHandler
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity
import org.jmolecules.event.annotation.DomainEventPublisher

@Entity
class BankAccountAggregate internal constructor(
  @Identity
  internal val accountId: AccountId,
  internal val bankAccount: BankAccount
) {

  companion object {
    /**
     * Factory method creating the bank account.
     */
    @DomainEventPublisher(
      publishes = "springboot.bank.BankAccountCreatedEvent", type = DomainEventPublisher.PublisherType.INTERNAL
    )
    @CommandHandler(namespace = "springboot.bank", name = "CreateBankAccountCommand")
    @JvmStatic
    fun handle(cmd: CreateBankAccountCommand): Pair<BankAccountAggregate, BankAccountCreatedEvent> {
      val (model, event) = BankAccount.createAccount(accountId = cmd.accountId, initialBalance = cmd.initialBalance)
      val aggregate = BankAccountAggregate(
        accountId = cmd.accountId,
        bankAccount = model
      )
      return aggregate to event
    }
  }

  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyDepositedEvent", type = DomainEventPublisher.PublisherType.INTERNAL
  )
  @CommandHandler(namespace = "springboot.bank", name = "DepositMoneyCommand")
  fun handle(cmd: DepositMoneyCommand): MoneyDepositedEvent {
    require(cmd.accountId == this.accountId) { "Wrong targeting of command ${cmd.accountId}, current bank account id is $accountId" }
    return bankAccount.depositMoney(cmd.amount)
  }

  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyWithdrawnEvent", type = DomainEventPublisher.PublisherType.INTERNAL
  )
  @CommandHandler(namespace = "springboot.bank", name = "WithdrawMoneyCommand")
  fun handle(cmd: WithdrawMoneyCommand): MoneyWithdrawnEvent {
    require(cmd.accountId == this.accountId) { "Wrong targeting of command ${cmd.accountId}, current bank account id is $accountId" }
    return bankAccount.withdrawMoney(cmd.amount)
  }

  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyTransferRequestedEvent", type = DomainEventPublisher.PublisherType.INTERNAL
  )
  @CommandHandler(namespace = "springboot.bank", name = "RequestMoneyTransferCommand")
  @Throws(InsufficientBalance::class)
  fun handle(cmd: RequestMoneyTransferCommand): MoneyTransferRequestedEvent {
    require(cmd.sourceAccountId == this.accountId) { "Wrong targeting of command ${cmd.sourceAccountId}, current bank account id is $accountId" }
    return bankAccount.requestMoneyTransfer(
      moneyTransferId = cmd.moneyTransferId,
      sourceAccountId = cmd.sourceAccountId,
      targetAccountId = cmd.targetAccountId,
      amount = cmd.amount
    )
  }

  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyTransferReceivedEvent", type = DomainEventPublisher.PublisherType.INTERNAL
  )
  @CommandHandler(namespace = "springboot.bank", name = "ReceiveMoneyTransferCommand")
  @Throws(MaximumBalanceExceeded::class)
  fun handle(cmd: ReceiveMoneyTransferCommand): MoneyTransferReceivedEvent {
    require(cmd.targetAccountId == this.accountId) { "Wrong targeting of command ${cmd.targetAccountId}, current bank account id is $accountId" }
    return bankAccount.receiveMoneyTransfer(
      moneyTransferId = cmd.moneyTransferId,
      sourceAccountId = cmd.sourceAccountId,
      targetAccountId = cmd.targetAccountId,
      amount = cmd.amount
    )
  }

  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyTransferCompletedEvent", type = DomainEventPublisher.PublisherType.INTERNAL
  )
  @CommandHandler(namespace = "springboot.bank", name = "CompleteMoneyTransferCommand")
  fun handle(cmd: CompleteMoneyTransferCommand): MoneyTransferCompletedEvent {
    require(cmd.sourceAccountId == this.accountId) { "Wrong targeting of command ${cmd.sourceAccountId}, current bank account id is $accountId" }
    return bankAccount.completeMoneyTransfer(
      moneyTransferId = cmd.moneyTransferId,
      sourceAccountId = cmd.sourceAccountId
    )
  }

  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyTransferCancelledEvent", type = DomainEventPublisher.PublisherType.INTERNAL
  )
  @CommandHandler(namespace = "springboot.bank", name = "ReceiveMoneyTransferCommand")
  fun handle(cmd: CancelMoneyTransferCommand): MoneyTransferCancelledEvent {
    require(cmd.sourceAccountId == this.accountId) { "Wrong targeting of command ${cmd.sourceAccountId}, current bank account id is $accountId" }
    return bankAccount.cancelMoneyTransfer(
      moneyTransferId = cmd.moneyTransferId,
      sourceAccountId = cmd.sourceAccountId,
      rejectionReason = cmd.rejectionReason
    )
  }
}
