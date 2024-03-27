package io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "MoneyTransferCompletedEvent")
data class MoneyTransferCompletedEvent(
        val moneyTransferId: MoneyTransferId,
        val sourceAccountId: AccountId,
        val amount: Amount
)
