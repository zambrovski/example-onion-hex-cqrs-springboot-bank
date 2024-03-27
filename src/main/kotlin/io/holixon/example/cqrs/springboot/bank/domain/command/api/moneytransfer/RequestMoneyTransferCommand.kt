package io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import org.jmolecules.architecture.cqrs.Command
import org.jmolecules.ddd.annotation.Association

/**
 * Received by the source account.
 * Reserves amount for transfer and starts saga.
 */
@Command(namespace = "springboot.bank", name = "RequestMoneyTransferCommand")
data class RequestMoneyTransferCommand(
        @Association
  val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: Amount,
        val moneyTransferId: MoneyTransferId
)
