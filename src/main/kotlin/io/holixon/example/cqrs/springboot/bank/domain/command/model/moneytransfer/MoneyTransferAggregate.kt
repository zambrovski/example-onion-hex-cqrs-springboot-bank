package io.holixon.example.cqrs.springboot.bank.domain.command.model.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CancelMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CompleteMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.ReceiveMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.RequestMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.model.BankAccountAggregate
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCancelledEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCompletedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferReceivedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferRequestedEvent
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.MaximumBalanceExceeded
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransfer
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import org.jmolecules.architecture.cqrs.CommandDispatcher
import org.jmolecules.ddd.annotation.Identity

class MoneyTransferAggregate private constructor(
  @Identity
  internal val moneyTransferId: MoneyTransferId,
  private val moneyTransfer: MoneyTransfer
) {

  companion object {
    @JvmStatic
    fun init(moneyTransferId: MoneyTransferId, sourceAccountId: AccountId, targetAccountId: AccountId, amount: Amount): MoneyTransferAggregate {
      return MoneyTransferAggregate(
        moneyTransferId = moneyTransferId,
        moneyTransfer = MoneyTransfer(
          sourceAccountId = sourceAccountId,
          targetAccountId = targetAccountId,
          amount = amount
        ))
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.RequestMoneyTransferCommand")
  fun request(sourceBankAccount: BankAccountAggregate): Result<MoneyTransferRequestedEvent> {
    if (sourceBankAccount.accountId != moneyTransfer.sourceAccountId) {
      return Result.failure(IllegalArgumentException("Expected the source account ${moneyTransfer.sourceAccountId}, but received ${sourceBankAccount.accountId}."))
    }
    return try {
      Result.success(
        sourceBankAccount.handle(
          RequestMoneyTransferCommand(
            moneyTransferId = moneyTransferId,
            sourceAccountId = moneyTransfer.sourceAccountId,
            targetAccountId = moneyTransfer.targetAccountId,
            amount = moneyTransfer.amount
          )
        )
      )
    } catch (e: InsufficientBalance) {
      Result.failure(e)
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.ReceiveMoneyTransferCommand")
  fun receive(targetBankAccount: BankAccountAggregate): Result<MoneyTransferReceivedEvent> {
    if (targetBankAccount.accountId != moneyTransfer.targetAccountId) {
      return Result.failure(IllegalArgumentException("Expected the target account ${moneyTransfer.targetAccountId}, but received ${targetBankAccount.accountId}."))
    }
    return try {
      Result.success(
        targetBankAccount.handle(
          ReceiveMoneyTransferCommand(
            moneyTransferId = moneyTransferId,
            targetAccountId = this.moneyTransfer.targetAccountId,
            sourceAccountId = this.moneyTransfer.sourceAccountId,
            amount = this.moneyTransfer.amount
          )
        )
      )
    } catch (e: MaximumBalanceExceeded) {
      Result.failure(e)
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.CompleteMoneyTransferCommand")
  fun complete(sourceBankAccount: BankAccountAggregate): Result<MoneyTransferCompletedEvent> {
    if (sourceBankAccount.accountId != moneyTransfer.sourceAccountId) {
      return Result.failure(IllegalArgumentException("Expected the source account ${moneyTransfer.sourceAccountId}, but received ${sourceBankAccount.accountId}."))
    }
    return Result.success(
      sourceBankAccount.handle(
        CompleteMoneyTransferCommand(
          moneyTransferId = moneyTransferId,
          sourceAccountId = this.moneyTransfer.sourceAccountId
        )
      )
    )
  }

  @CommandDispatcher(dispatches = "springboot.bank.CancelMoneyTransferCommand")
  fun cancel(sourceBankAccount: BankAccountAggregate, rejectionReason: RejectionReason): Result<MoneyTransferCancelledEvent> {
    if (sourceBankAccount.accountId != moneyTransfer.sourceAccountId) {
      return Result.failure(IllegalArgumentException("Expected the source account ${moneyTransfer.sourceAccountId}, but received ${sourceBankAccount.accountId}."))
    }
    return Result.success(
      sourceBankAccount.handle(
        CancelMoneyTransferCommand(
          moneyTransferId = moneyTransferId,
          sourceAccountId = this.moneyTransfer.sourceAccountId,
          targetAccountId = this.moneyTransfer.targetAccountId,
          rejectionReason = rejectionReason
        )
      )
    )
  }

}
