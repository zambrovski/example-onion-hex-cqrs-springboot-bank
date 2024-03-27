package io.holixon.example.cqrs.springboot.bank.domain.command.model.account

import io.holixon.example.cqrs.springboot.bank.domain.command.model.account.BankAccountCreationVerificationResult.*
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Balance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.ReservedAmount

/**
 * Balance of a bank account: current and the upper and lower limits.
 */
data class BankAccountBalance(
  internal val currentBalance: Balance,
  internal val maximumBalance: Balance,
  internal val minimumBalance: Balance
) {

  companion object {

    fun validateInitialBalance(
      balance: Balance,
      maximumBalance: Balance,
      minimumBalance: Balance
    ): BankAccountCreationVerificationResult {
      return when {
        balance < minimumBalance -> InsufficientBalanceAmountVerificationResult(
          minimumBalance
        )

        balance > maximumBalance -> BalanceAmountExceededVerificationResult(
          maximumBalance
        )

        else -> ValidBalanceAmountVerificationResult
      }
    }
  }


  /**
   * Is increase possible?
   */
  fun canIncrease(amount: Amount) = currentBalance + amount <= maximumBalance

  /**
   * Is decrease possible?
   */
  fun canDecrease(amount: Amount, reserved: ReservedAmount) = currentBalance - reserved - amount >= minimumBalance

  /**
   * Increase amount.
   */
  fun increase(amount: Amount) = copy(currentBalance = currentBalance + amount)

  /**
   * Decrease amount.
   */
  fun decrease(amount: Amount) = copy(currentBalance = currentBalance - amount)
}
