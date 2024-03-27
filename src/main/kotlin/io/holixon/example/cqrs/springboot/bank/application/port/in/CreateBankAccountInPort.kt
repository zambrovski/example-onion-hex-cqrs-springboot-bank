package io.holixon.example.cqrs.springboot.bank.application.port.`in`

import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Balance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import java.util.concurrent.CompletableFuture

/**
 * Port to address UC-001 Create Bank Account.
 */
interface CreateBankAccountInPort {
  /**
   * Creates a bank account.
   * @param accountId account id.
   * @param initialBalance balance of the account.
   */
  @Throws(MaximumBalanceExceeded::class, InsufficientBalance::class)
  fun createBankAccount(accountId: AccountId, initialBalance: Balance): CompletableFuture<Unit>
}
