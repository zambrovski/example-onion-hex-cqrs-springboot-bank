package io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId

class MoneyTransferNotFound(
  val accountId: AccountId,
  val moneyTransferId: MoneyTransferId
) : RuntimeException("BankAccount[id=$accountId] is not part of transfer=$moneyTransferId")
