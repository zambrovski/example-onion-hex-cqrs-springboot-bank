package io.holixon.example.cqrs.springboot.bank.application.port.`in`

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.CurrentBalance
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Port to access UC-005.
 */
interface RetrieveBankAccountInformationInPort {
  /**
   * Retrieves the current balance of the bank account.
   * @param accountId account id.
   * @return current balance.
   */
  fun getCurrentBalance(accountId: AccountId): CompletableFuture<Optional<CurrentBalance>>
}
