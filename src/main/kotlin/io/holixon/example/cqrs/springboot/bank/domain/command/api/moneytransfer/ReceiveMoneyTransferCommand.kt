package io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import org.jmolecules.architecture.cqrs.Command
import org.jmolecules.ddd.annotation.Association

/**
 * Invoked by the saga, recipient is target account.
 */
@Command(namespace = "springboot.bank", name = "ReceiveMoneyTransferCommand")
data class ReceiveMoneyTransferCommand(
        @Association
  val targetAccountId: AccountId,
        val sourceAccountId: AccountId,
        val moneyTransferId: MoneyTransferId,
        val amount: Amount
)
