package io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import org.jmolecules.architecture.cqrs.Command
import org.jmolecules.ddd.annotation.Association

/**
 * Internal command to complete money transfer.
 */
@Command(namespace = "springboot.bank", name = "CompleteMoneyTransferCommand")
data class CompleteMoneyTransferCommand(
        @Association
  val sourceAccountId: AccountId,
        val moneyTransferId: MoneyTransferId
)
