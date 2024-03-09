package io.holixon.example.cqrs.springboot.bank.domain.command.api

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
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
