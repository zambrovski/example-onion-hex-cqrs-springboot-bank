package io.holixon.example.cqrs.springboot.bank.application.port.`in`

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import java.util.concurrent.CompletableFuture

/**
 * Port to address UC-003 Withdraw Money (ATM).
 */
interface WithdrawMoneyInPort {
  /**
   * Withdraws money from given account.
   * @param accountId account id.
   * @param amount amount to withdraw.
   */
  @Throws(InsufficientBalance::class)
  fun withdrawMoney(accountId: AccountId, amount: Amount): CompletableFuture<Unit>
}
