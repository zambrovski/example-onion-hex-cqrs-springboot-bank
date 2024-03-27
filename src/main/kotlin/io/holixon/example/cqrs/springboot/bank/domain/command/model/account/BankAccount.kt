package io.holixon.example.cqrs.springboot.bank.domain.command.model.account

import io.holixon.example.cqrs.springboot.bank.domain.command.model.account.BankAccountCreationVerificationResult.*
import io.holixon.example.cqrs.springboot.bank.domain.event.account.BankAccountCreatedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.atm.MoneyDepositedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.atm.MoneyWithdrawnEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCancelledEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferCompletedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferReceivedEvent
import io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferRequestedEvent
import io.holixon.example.cqrs.springboot.bank.domain.type.*
import io.holixon.example.cqrs.springboot.bank.domain.type.account.*
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.BadMoneyTransfer
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferNotFound
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason

/**
 * Represents bank account.
 */
class BankAccount(
  private val accountId: AccountId,
  private var balanceModel: BankAccountBalance,
  private val activeMoneyTransfers: ActiveMoneyTransfers
) {

  companion object {
    /**
     * We do not lend money. Never. To anyone.
     */
    private val MIN_BALANCE = Balance.of(0)

    /**
     * We believe that no one will ever need more money than this. (this rule allows easier testing of failures on transfers).
     */
    private val MAX_BALANCE = Balance.of(1000)

    @Throws(
      InsufficientBalance::class,
      MaximumBalanceExceeded::class
    )
    fun createAccount(accountId: AccountId, initialBalance: Balance): Pair<BankAccount, BankAccountCreatedEvent> {

      // validate and create the event or throw
      val event = when (val result = BankAccountBalance.validateInitialBalance(
        balance = initialBalance,
        maximumBalance = MAX_BALANCE,
        minimumBalance = MIN_BALANCE
      )) {
        is InsufficientBalanceAmountVerificationResult -> throw InsufficientBalance(
          accountId = accountId,
          withdrawAmount = null,
          currentBalance = initialBalance,
          minimumBalance = result.minimumBalance
        )

        is BalanceAmountExceededVerificationResult -> throw MaximumBalanceExceeded(
          accountId = accountId,
          depositAmount = null,
          currentBalance = initialBalance,
          maximumBalance = result.maximumBalance
        )

        ValidBalanceAmountVerificationResult -> BankAccountCreatedEvent(
          accountId = accountId,
          initialBalance = initialBalance
        )
      }

      val model = BankAccount(
        accountId = accountId,
        balanceModel = BankAccountBalance(
          currentBalance = initialBalance,
          maximumBalance = MAX_BALANCE,
          minimumBalance = MIN_BALANCE
        ),
        activeMoneyTransfers = ActiveMoneyTransfers()
      )

      return model to event
    }
  }

  @Throws(MaximumBalanceExceeded::class)
  fun depositMoney(amount: Amount): MoneyDepositedEvent {
    // validate
    if (!balanceModel.canIncrease(amount)) {
      throw MaximumBalanceExceeded(
        accountId = accountId,
        currentBalance = balanceModel.currentBalance,
        maximumBalance = balanceModel.maximumBalance,
        depositAmount = amount
      )
    }
    // mutate
    increaseBalance(amount = amount)

    // event
    return MoneyDepositedEvent(
      accountId = accountId,
      amount = amount
    )
  }

  @Throws(InsufficientBalance::class)
  fun withdrawMoney(amount: Amount): MoneyWithdrawnEvent {
    // validate
    if (!balanceModel.canDecrease(amount, activeMoneyTransfers.getReservedAmount())) {
      throw InsufficientBalance(
        accountId = accountId,
        currentBalance = balanceModel.currentBalance,
        withdrawAmount = amount,
        minimumBalance = balanceModel.minimumBalance
      )
    }
    // mutate
    decreaseBalance(amount = amount)

    // event
    return MoneyWithdrawnEvent(
      accountId = accountId,
      amount = amount
    )
  }

  @Throws(InsufficientBalance::class, BadMoneyTransfer::class)
  fun requestMoneyTransfer(
    moneyTransferId: MoneyTransferId,
    sourceAccountId: AccountId,
    targetAccountId: AccountId,
    amount: Amount
  ): MoneyTransferRequestedEvent {

    if (sourceAccountId == targetAccountId || this.accountId != sourceAccountId) {
      throw BadMoneyTransfer(
        accountId = sourceAccountId,
        moneyTransferId = moneyTransferId
      )
    }

    if (!balanceModel.canDecrease(amount, activeMoneyTransfers.getReservedAmount())) {
      throw InsufficientBalance(
        accountId = accountId,
        currentBalance = balanceModel.currentBalance,
        withdrawAmount = amount,
        minimumBalance = balanceModel.minimumBalance
      )
    }

    // mutate
    initializeMoneyTransfer(moneyTransferId = moneyTransferId, amount = amount)

    // event
    return MoneyTransferRequestedEvent(
      moneyTransferId = moneyTransferId,
      sourceAccountId = sourceAccountId,
      targetAccountId = targetAccountId,
      amount = amount
    )
  }

  @Throws(MaximumBalanceExceeded::class)
  fun receiveMoneyTransfer(
    moneyTransferId: MoneyTransferId,
    sourceAccountId: AccountId,
    targetAccountId: AccountId,
    amount: Amount
  ): MoneyTransferReceivedEvent {
    // validate correct dispatching
    if (sourceAccountId == targetAccountId || this.accountId != targetAccountId) {
      throw BadMoneyTransfer(
        accountId = targetAccountId,
        moneyTransferId = moneyTransferId
      )
    }

    // validate
    if (!balanceModel.canIncrease(amount)) {
      throw MaximumBalanceExceeded(
        accountId = accountId,
        currentBalance = balanceModel.currentBalance,
        maximumBalance = balanceModel.maximumBalance,
        depositAmount = amount
      )
    }

    // mutate
    increaseBalance(amount = amount)

    // event
    return MoneyTransferReceivedEvent(
      moneyTransferId = moneyTransferId,
      sourceAccountId = sourceAccountId,
      targetAccountId = targetAccountId,
      amount = amount
    )
  }

  fun completeMoneyTransfer(moneyTransferId: MoneyTransferId, sourceAccountId: AccountId): MoneyTransferCompletedEvent {
    //
    val amount = activeMoneyTransfers.getAmountForTransfer(moneyTransferId) ?: throw MoneyTransferNotFound(
      accountId = accountId,
      moneyTransferId = moneyTransferId
    )

    // mutate
    decreaseBalance(amount = amount)
    acknowledgeMoneyTransferCompletion(moneyTransferId = moneyTransferId)

    // event
    return MoneyTransferCompletedEvent(
      moneyTransferId = moneyTransferId,
      sourceAccountId = sourceAccountId,
      amount = amount
    )
  }

  fun cancelMoneyTransfer(
    moneyTransferId: MoneyTransferId,
    sourceAccountId: AccountId,
    rejectionReason: RejectionReason
  ): MoneyTransferCancelledEvent {
    // validate
    if (!checkMoneyTransfer(moneyTransferId)) {
      throw MoneyTransferNotFound(accountId = sourceAccountId, moneyTransferId = moneyTransferId)
    }

    // mutate
    acknowledgeMoneyTransferCancellation(moneyTransferId = moneyTransferId)

    // event
    return MoneyTransferCancelledEvent(
      moneyTransferId = moneyTransferId,
      reason = rejectionReason
    )
  }

  private fun checkMoneyTransfer(moneyTransferId: MoneyTransferId) =
    activeMoneyTransfers.hasMoneyTransfer(moneyTransferId)


  internal fun getCurrentBalance(): Balance = this.balanceModel.currentBalance

  internal fun getActiveMoneyTransfers(): ActiveMoneyTransfers = this.activeMoneyTransfers

  // -----------------------------------------------------------
  // Model modification

  private fun increaseBalance(amount: Amount) {
    balanceModel = balanceModel.increase(amount)
  }

  private fun decreaseBalance(amount: Amount) {
    balanceModel = balanceModel.decrease(amount)
  }

  private fun initializeMoneyTransfer(moneyTransferId: MoneyTransferId, amount: Amount) {
    activeMoneyTransfers.initTransfer(moneyTransferId = moneyTransferId, amount = amount)
  }

  private fun acknowledgeMoneyTransferCompletion(moneyTransferId: MoneyTransferId) {
    activeMoneyTransfers.completeTransfer(moneyTransferId)
  }

  private fun acknowledgeMoneyTransferCancellation(moneyTransferId: MoneyTransferId) {
    activeMoneyTransfers.cancelTransfer(moneyTransferId)
  }

}

