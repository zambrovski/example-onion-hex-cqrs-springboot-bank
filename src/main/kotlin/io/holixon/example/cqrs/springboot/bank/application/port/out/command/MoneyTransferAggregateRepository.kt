package io.holixon.example.cqrs.springboot.bank.application.port.out.command

import io.holixon.example.cqrs.springboot.bank.domain.command.model.moneytransfer.MoneyTransferAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId

interface MoneyTransferAggregateRepository {

  fun load(moneyTransferId: MoneyTransferId): MoneyTransferAggregate

  fun save(moneyTransferAggregate: MoneyTransferAggregate): MoneyTransferAggregate
}
