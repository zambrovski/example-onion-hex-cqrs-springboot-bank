package io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount

/**
 * Money transfer model.
 */
data class MoneyTransferSummary(
  val moneyTransferId: MoneyTransferId,
  val sourceAccountId: AccountId,
  val targetAccountId: AccountId,
  val amount: Amount,
  val status: MoneyTransferStatus
)
