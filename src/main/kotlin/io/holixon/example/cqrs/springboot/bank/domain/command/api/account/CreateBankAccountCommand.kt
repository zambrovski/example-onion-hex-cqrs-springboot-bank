package io.holixon.example.cqrs.springboot.bank.domain.command.api.account

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Balance
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
