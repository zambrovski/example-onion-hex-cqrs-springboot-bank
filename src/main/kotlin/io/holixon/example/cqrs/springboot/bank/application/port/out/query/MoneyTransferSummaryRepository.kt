package io.holixon.example.cqrs.springboot.bank.application.port.out.query

import org.jmolecules.ddd.annotation.Repository
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.query.BankAccountMoneyTransfer
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import java.util.*

/**
 * Repository for money transfers.
 */
@Repository
interface MoneyTransferSummaryRepository {
  /**
   * Finds transfer by id.
   * @param id id of the transfer
   * @return transfer option
   */
  fun findById(id: MoneyTransferId): Optional<BankAccountMoneyTransfer>

  /**
   * Finds all money transfers for given account.
   * @param accountId id of the account.
   * @return list of all transfers.
   */
  fun findByAccountId(accountId: AccountId): List<BankAccountMoneyTransfer>

  /**
   * Finds all money transfers.
   * @return list of all transfers.
   */
  fun findAll(): List<BankAccountMoneyTransfer>

  /**
   * Stores a money transfer.
   * @param entity transfer to store.
   */
  fun save(entity: BankAccountMoneyTransfer)
}
