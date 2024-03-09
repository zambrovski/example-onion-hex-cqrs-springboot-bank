package io.holixon.example.cqrs.springboot.bank.application.port.out.query

import io.holixon.example.cqrs.springboot.bank.domain.query.BankAccountCurrentBalance
import org.jmolecules.ddd.annotation.Repository
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import java.util.*

/**
 * Repository for bank account current balances.
 */
@Repository
interface BankAccountCurrentBalanceRepository {
  /**
   * Finds the bank account balance.
   * @param accountId bank account id.
   */
  fun findById(accountId: AccountId): Optional<BankAccountCurrentBalance>

  /**
   * Stores bank account current balance.
   * @param entity current balance of the bank account to store.
   */
  fun save(entity: BankAccountCurrentBalance)
}
