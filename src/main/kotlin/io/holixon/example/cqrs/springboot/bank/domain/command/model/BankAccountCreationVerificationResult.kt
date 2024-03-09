package io.holixon.example.cqrs.springboot.bank.domain.command.model

import io.holixon.example.cqrs.springboot.bank.domain.type.Balance

/**
 * Bank account verification result.
 */
sealed class BankAccountCreationVerificationResult {

  /**
   * Exceeded balance.
   */
  data class BalanceAmountExceededVerificationResult(
    val maximumBalance: Balance
  ) : BankAccountCreationVerificationResult()

  /**
   * Insufficient balance.
   */
  data class InsufficientBalanceAmountVerificationResult(
    val minimumBalance: Balance
  ) : BankAccountCreationVerificationResult()

  /**
   * Valid account.
   */
  object ValidBalanceAmountVerificationResult : BankAccountCreationVerificationResult()

}
