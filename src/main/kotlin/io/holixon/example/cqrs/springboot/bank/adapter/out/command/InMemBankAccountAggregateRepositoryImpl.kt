package io.holixon.example.cqrs.springboot.bank.adapter.out.command

import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.domain.command.model.BankAccountAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId

class InMemBankAccountAggregateRepositoryImpl(
  private val storage: MutableMap<AccountId, BankAccountAggregate> = mutableMapOf()
) : BankAccountAggregateRepository {

  override fun load(accountId: AccountId): BankAccountAggregate {
    return storage[accountId] ?: throw NoSuchElementException("Could not find bank account with id $accountId")
  }

  override fun save(bankAccount: BankAccountAggregate): BankAccountAggregate {
    storage[bankAccount.accountId] = bankAccount
    return storage[bankAccount.accountId] ?: throw NoSuchElementException("Could not find bank account with id ${bankAccount.accountId}")
  }
}
