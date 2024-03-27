package io.holixon.example.cqrs.springboot.bank.domain.command.model

import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CancelMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.CompleteMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.ReceiveMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer.RequestMoneyTransferCommand
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCancelledEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCompletedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferReceivedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferRequestedEvent
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import io.holixon.example.cqrs.springboot.bank.domain.command.model.moneytransfer.MoneyTransferRequest
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import org.jmolecules.architecture.cqrs.CommandDispatcher
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity

@Entity
class MoneyTransferAggregate private constructor(
  @Identity
  internal val moneyTransferId: MoneyTransferId,
  private val moneyTransferRequest: MoneyTransferRequest
) {

  companion object {
    @JvmStatic
    fun init(moneyTransferId: MoneyTransferId, sourceAccountId: AccountId, targetAccountId: AccountId, amount: Amount): MoneyTransferAggregate {
      return MoneyTransferAggregate(
        moneyTransferId = moneyTransferId,
        moneyTransferRequest = MoneyTransferRequest(
          sourceAccountId = sourceAccountId,
          targetAccountId = targetAccountId,
          amount = amount
        ))
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.RequestMoneyTransferCommand")
  fun request(sourceBankAccount: BankAccountAggregate): Result<MoneyTransferRequestedEvent> {
    return if (sourceBankAccount.accountId != moneyTransferRequest.sourceAccountId) {
      Result.failure(IllegalArgumentException("Expected the source account ${moneyTransferRequest.sourceAccountId}, but received ${sourceBankAccount.accountId}."))
    } else {
      try {
        Result.success(
          sourceBankAccount.handle(
            RequestMoneyTransferCommand(
              moneyTransferId = moneyTransferId,
              sourceAccountId = moneyTransferRequest.sourceAccountId,
              targetAccountId = moneyTransferRequest.targetAccountId,
              amount = moneyTransferRequest.amount
            )
          )
        )
      } catch (e: InsufficientBalance) {
        Result.failure(e)
      }
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.ReceiveMoneyTransferCommand")
  fun receive(targetBankAccount: BankAccountAggregate): Result<MoneyTransferReceivedEvent> {
    return if (targetBankAccount.accountId != moneyTransferRequest.targetAccountId) {
      Result.failure(IllegalArgumentException("Expected the target account ${moneyTransferRequest.targetAccountId}, but received ${targetBankAccount.accountId}."))
    } else {
      try {
        Result.success(
          targetBankAccount.handle(
            ReceiveMoneyTransferCommand(
              moneyTransferId = moneyTransferId,
              targetAccountId = this.moneyTransferRequest.targetAccountId,
              sourceAccountId = this.moneyTransferRequest.sourceAccountId,
              amount = this.moneyTransferRequest.amount
            )
          )
        )
      } catch (e: MaximumBalanceExceeded) {
        Result.failure(e)
      }
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.CompleteMoneyTransferCommand")
  fun complete(sourceBankAccount: BankAccountAggregate): Result<MoneyTransferCompletedEvent> {
    return if (sourceBankAccount.accountId != moneyTransferRequest.sourceAccountId) {
      Result.failure(IllegalArgumentException("Expected the source account ${moneyTransferRequest.sourceAccountId}, but received ${sourceBankAccount.accountId}."))
    } else {
      Result.success(
        sourceBankAccount.handle(
          CompleteMoneyTransferCommand(
            moneyTransferId = moneyTransferId,
            sourceAccountId = this.moneyTransferRequest.sourceAccountId
          )
        )
      )
    }
  }

  @CommandDispatcher(dispatches = "springboot.bank.CancelMoneyTransferCommand")
  fun cancel(sourceBankAccount: BankAccountAggregate, rejectionReason: RejectionReason): Result<MoneyTransferCancelledEvent> {
    return if (sourceBankAccount.accountId != moneyTransferRequest.sourceAccountId) {
      Result.failure(IllegalArgumentException("Expected the source account ${moneyTransferRequest.sourceAccountId}, but received ${sourceBankAccount.accountId}."))
    } else {
      Result.success(
        sourceBankAccount.handle(
          CancelMoneyTransferCommand(
            moneyTransferId = moneyTransferId,
            sourceAccountId = this.moneyTransferRequest.sourceAccountId,
            targetAccountId = this.moneyTransferRequest.targetAccountId,
            rejectionReason = rejectionReason
          )
        )
      )
    }
  }

}
