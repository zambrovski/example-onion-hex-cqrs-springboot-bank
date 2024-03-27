package io.holixon.example.cqrs.springboot.bank.domain.command.api.atm

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import org.jmolecules.architecture.cqrs.Command
import org.jmolecules.ddd.annotation.Association

/**
 * Deposit money command.
 */
@Command(namespace = "springboot.bank", name = "DepositMoneyCommand")
data class DepositMoneyCommand(
  @Association
  val accountId: AccountId,
  val amount: Amount
)
