package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.RetrieveBankAccountInformationInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.domain.query.BankAccountCurrentBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.CurrentBalance
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Account information retrieval use case.
 */
class RetrieveAccountInformationUseCase(
  private val bankAccountCurrentBalanceRepository: BankAccountCurrentBalanceRepository
) : RetrieveBankAccountInformationInPort {

  /**
   * Retrieves the current balance of the bank account.
   * @param accountId account id.
   * @return current balance.
   */
  override fun getCurrentBalance(accountId: AccountId): CompletableFuture<Optional<CurrentBalance>> {
    return CompletableFuture.completedFuture(
      bankAccountCurrentBalanceRepository.findById(accountId).map { it.toDomain() }
    )
  }

  private fun BankAccountCurrentBalance.toDomain() = CurrentBalance(
    accountId = this.accountId,
    currentBalance = this.balance
  )

}
