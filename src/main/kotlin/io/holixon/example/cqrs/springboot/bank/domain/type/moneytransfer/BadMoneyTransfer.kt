package io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId

class BadMoneyTransfer(
  val accountId: AccountId,
  val moneyTransferId: MoneyTransferId
) : RuntimeException("Bad money transfer=$moneyTransferId. Source and target account id must be different, but both are equals $accountId")
