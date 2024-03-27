package io.holixon.example.cqrs.springboot.bank.domain.query.model

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity

@Entity
class BankAccountMoneyTransfer(
  @Identity
  val moneyTransferId: MoneyTransferId,
  val sourceAccountId: AccountId,
  val targetAccountId: AccountId,
  val amount: Amount,
  var success: Boolean? = null,
  var errorMessage: RejectionReason? = null
) {
  /**
   * Completes money transfer.
   * @return money transfer marked as completed.
   */
  fun complete(): BankAccountMoneyTransfer {
    return this.apply {
      success = true
    }
  }

  /**
   * Cancels money transfer.
   * @param rejectionReason reason for rejection.
   * @return money transfer marked as cancelled.
   */
  fun cancel(rejectionReason: RejectionReason): BankAccountMoneyTransfer {
    return this.apply {
      success = false
      errorMessage = rejectionReason
    }
  }
}
