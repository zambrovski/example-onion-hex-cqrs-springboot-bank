package io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "MoneyTransferFailedEvent")
data class MoneyTransferFailedEvent(
        val moneyTransferId: MoneyTransferId,
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: Amount,
        val reason: RejectionReason
)
