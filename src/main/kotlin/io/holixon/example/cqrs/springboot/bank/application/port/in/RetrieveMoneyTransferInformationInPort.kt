package io.holixon.example.cqrs.springboot.bank.application.port.`in`

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferNotFound
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferSummaries
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferSummary
import java.util.*
import java.util.concurrent.CompletableFuture

interface RetrieveMoneyTransferInformationInPort {
  /**
   * Retrieves a list of money transfers for given account.
   * @param accountId account id.
   * @throws [MoneyTransferNotFound] if no money transfer can be found.
   * @return list of transfers, the account is part of.
   */
  @Throws(MoneyTransferNotFound::class)
  fun getMoneyTransfers(accountId: AccountId): CompletableFuture<MoneyTransferSummaries>

  /**
   * Finds a money transfer.
   * @param moneyTransferId id of the money transfer.
   * @throws [MoneyTransferNotFound] if no money transfer can be found.
   * @return money transfer.
   */
  @Throws(MoneyTransferNotFound::class)
  fun getMoneyTransfer(moneyTransferId: MoneyTransferId): CompletableFuture<Optional<MoneyTransferSummary>>

}
