package io.holixon.example.cqrs.springboot.bank.domain.command.api

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Balance
import org.jmolecules.architecture.cqrs.Command
import org.jmolecules.ddd.annotation.Association

/**
 * Create a new bank account command.
 */
@Command(namespace = "springboot.bank", name = "CreateBankAccountCommand")
data class CreateBankAccountCommand(
  @Association
  val accountId: AccountId,
  val initialBalance: Balance
)
