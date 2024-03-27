package io.holixon.example.cqrs.springboot.bank.adapter.out.command

import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferAggregateRepository
import io.holixon.example.cqrs.springboot.bank.domain.command.model.MoneyTransferAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId

class InMemMoneyTransferAggregateRepositoryImpl(
  private val storage: MutableMap<MoneyTransferId, MoneyTransferAggregate> = mutableMapOf()
) : MoneyTransferAggregateRepository {

  override fun load(moneyTransferId: MoneyTransferId): MoneyTransferAggregate {
    return storage[moneyTransferId] ?: throw NoSuchElementException("Could not find money transfer with id $moneyTransferId")
  }

  override fun save(moneyTransferAggregate: MoneyTransferAggregate): MoneyTransferAggregate {
    storage[moneyTransferAggregate.moneyTransferId] = moneyTransferAggregate
    return storage[moneyTransferAggregate.moneyTransferId]
      ?: throw NoSuchElementException("Could not find money transfer with id ${moneyTransferAggregate.moneyTransferId}")
  }
}
