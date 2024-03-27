package io.holixon.example.cqrs.springboot.bank.application.port.`in`

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import java.util.concurrent.CompletableFuture

/**
 * Port to address UC-004 Perform a money transfer.
 */
interface TransferMoneyInPort {
  /**
   * Initializes money transfers from source to target account.
   * @param sourceAccountId source account id.
   * @param targetAccountId target account id.
   * @param amount amount to transfer.
   * @return money transfer id.
   * @throws [InsufficientBalance] if the balance source account would be below the minimum.
   * @throws [MaximumBalanceExceeded] if the balance target account would be above the maximum.
   */
  @Throws(InsufficientBalance::class, MaximumBalanceExceeded::class)
  fun transferMoney(sourceAccountId: AccountId, targetAccountId: AccountId, amount: Amount): CompletableFuture<MoneyTransferId>
}
