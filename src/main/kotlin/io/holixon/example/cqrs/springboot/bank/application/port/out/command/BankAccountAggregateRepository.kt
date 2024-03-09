package io.holixon.example.cqrs.springboot.bank.application.port.out.command

import io.holixon.example.cqrs.springboot.bank.domain.command.model.BankAccountAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import org.jmolecules.ddd.annotation.Repository

/**
 * Repository for bank accounts.
 */
@Repository
interface BankAccountAggregateRepository {

  /**
   * Loads bank account by account id.
   * @param accountId bank account id
   * @return bank account aggregate
   */
  fun load(accountId: AccountId): BankAccountAggregate

  /**
   * Stores bank account.
   * @param bankAccount bank account.
   */
  fun save(bankAccount: BankAccountAggregate): BankAccountAggregate

}
