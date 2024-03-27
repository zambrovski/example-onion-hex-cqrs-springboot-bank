package io.holixon.example.cqrs.springboot.bank.domain.command.api.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import org.jmolecules.architecture.cqrs.Command
import org.jmolecules.ddd.annotation.Association

/**
 * Internal command to cancel money transfer.
 */
@Command(namespace = "springboot.bank", name = "CancelMoneyTransferCommand")
data class CancelMoneyTransferCommand(
  @Association
  val sourceAccountId: AccountId,
  val moneyTransferId: MoneyTransferId,
  val targetAccountId: AccountId,
  val rejectionReason: RejectionReason,
)
