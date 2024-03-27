package io.holixon.example.cqrs.springboot.bank.application.port.`in`

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import java.util.concurrent.CompletableFuture

/**
 * Port to address UC-002 Deposit Money (ATM).
 */
interface DepositMoneyInPort {
  /**
   * Deposits money on the given account.
   * @param accountId account id.
   * @param amount amount to deposit.
   */
  @Throws(MaximumBalanceExceeded::class)
  fun depositMoney(accountId: AccountId, amount: Amount): CompletableFuture<Unit>
}
