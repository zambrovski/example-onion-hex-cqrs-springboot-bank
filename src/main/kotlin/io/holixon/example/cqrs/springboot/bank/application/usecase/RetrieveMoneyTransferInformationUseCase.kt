package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.RetrieveMoneyTransferInformationInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import io.holixon.example.cqrs.springboot.bank.domain.query.api.MoneyTransferSummaries
import io.holixon.example.cqrs.springboot.bank.domain.query.api.MoneyTransferSummary
import io.holixon.example.cqrs.springboot.bank.domain.query.model.BankAccountMoneyTransfer
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.*
import java.util.*
import java.util.concurrent.CompletableFuture

class RetrieveMoneyTransferInformationUseCase(
  private val moneyTransferSummaryRepository: MoneyTransferSummaryRepository
) : RetrieveMoneyTransferInformationInPort {

  /**
   * Retrieves a list of money transfers for given account.
   * @param accountId account id.
   * @throws [MoneyTransferNotFound] if no money transfer can be found.
   * @return list of transfers, the account is part of.
   */
  @Throws(MoneyTransferNotFound::class)
  override fun getMoneyTransfers(accountId: AccountId): CompletableFuture<MoneyTransferSummaries> {
    return CompletableFuture.completedFuture(
      MoneyTransferSummaries(
        moneyTransferSummaryRepository.findByAccountId(accountId = accountId).map { it.toDomain() }
      )
    )
  }

  /**
   * Finds a money transfer.
   * @param moneyTransferId id of the money transfer.
   * @throws [MoneyTransferNotFound] if no money transfer can be found.
   * @return money transfer.
   */
  @Throws(MoneyTransferNotFound::class)
  override fun getMoneyTransfer(moneyTransferId: MoneyTransferId): CompletableFuture<Optional<MoneyTransferSummary>> {
    return CompletableFuture.completedFuture(moneyTransferSummaryRepository.findById(id = moneyTransferId).map { it.toDomain() })
  }


  private fun BankAccountMoneyTransfer.toDomain() = MoneyTransferSummary(
    moneyTransferId = this.moneyTransferId,
    sourceAccountId = this.sourceAccountId,
    targetAccountId = this.targetAccountId,
    amount = this.amount,
    status = MoneyTransferStatus.of(this.success, this.errorMessage),
  )

}
