package io.holixon.example.cqrs.springboot.bank.adapter.out.query

import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.domain.query.model.BankAccountCurrentBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Simple in-memory repository implementation.
 */
class InMemBankAccountCurrentBalanceRepositoryImpl : BankAccountCurrentBalanceRepository {

  private val store: MutableMap<AccountId, BankAccountCurrentBalance> = ConcurrentHashMap()

  override fun findById(accountId: AccountId): Optional<BankAccountCurrentBalance> {
    return Optional.ofNullable(store[accountId])
  }

  override fun save(entity: BankAccountCurrentBalance) {
    store[entity.accountId] = entity
  }
}
