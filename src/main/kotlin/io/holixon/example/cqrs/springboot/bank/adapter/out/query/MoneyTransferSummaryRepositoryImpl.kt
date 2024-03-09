package io.holixon.example.cqrs.springboot.bank.adapter.out.query

import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import io.holixon.example.cqrs.springboot.bank.domain.query.BankAccountMoneyTransfer
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Simple in memory implementation.
 */
class MoneyTransferSummaryRepositoryImpl : MoneyTransferSummaryRepository {

  private val store: MutableMap<MoneyTransferId, BankAccountMoneyTransfer> = ConcurrentHashMap()

  override fun findById(id: MoneyTransferId): Optional<BankAccountMoneyTransfer> {
    return Optional.ofNullable(store[id])
  }

  override fun findByAccountId(accountId: io.holixon.example.cqrs.springboot.bank.domain.type.AccountId): List<BankAccountMoneyTransfer> {
    return store.values
      .filter { it.sourceAccountId == accountId || it.targetAccountId == accountId }
      .toList()
  }

  override fun findAll(): List<BankAccountMoneyTransfer> {
    return store.values.toList()
  }

  override fun save(entity: BankAccountMoneyTransfer) {
    store[entity.moneyTransferId] = entity
  }
}
