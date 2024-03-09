package io.holixon.example.cqrs.springboot.bank.domain.type

/**
 * Indicates insufficient balance.
 */
class InsufficientBalance(
  val accountId: io.holixon.example.cqrs.springboot.bank.domain.type.AccountId,
  val currentBalance: Balance,
  val withdrawAmount: Amount?,
  val minimumBalance: Balance
) : RuntimeException(
  if (withdrawAmount == null) {
    "BankAccount[id=$accountId] can't be created, its balance $currentBalance would subceed min. balance of $minimumBalance"
  } else {
    "BankAccount[id=$accountId, currentBalance=$currentBalance]: Withdrawal of amount=$withdrawAmount not allowed, would subceed min. balance of $minimumBalance"
  }
)
