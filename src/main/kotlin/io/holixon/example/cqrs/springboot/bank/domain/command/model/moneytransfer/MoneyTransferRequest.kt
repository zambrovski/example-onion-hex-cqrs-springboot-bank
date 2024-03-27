package io.holixon.example.cqrs.springboot.bank.domain.command.model.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount

/**
 * Money transfer.
 */
data class MoneyTransferRequest(
  val sourceAccountId: AccountId,
  val targetAccountId: AccountId,
  val amount: Amount
)
