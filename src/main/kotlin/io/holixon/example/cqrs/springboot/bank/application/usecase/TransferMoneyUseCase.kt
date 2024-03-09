package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.TransferMoneyInPort
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import mu.KLogging
import java.util.concurrent.CompletableFuture

/**
 * Operations related to money transfers.
 */
class TransferMoneyUseCase(
  private val moneyTransferProcessManager: MoneyTransferProcessManager
) : TransferMoneyInPort {

  companion object : KLogging()

  /**
   * Initializes money transfers from source to target account.
   * @param sourceAccountId source account id.
   * @param targetAccountId target account id.
   * @param amount amount to transfer.
   * @return money transfer id.
   */
  override fun transferMoney(sourceAccountId: AccountId, targetAccountId: AccountId, amount: Amount): CompletableFuture<MoneyTransferId> {
    val moneyTransferId = moneyTransferProcessManager.execute(sourceAccountId, targetAccountId, amount)
    return CompletableFuture.completedFuture(moneyTransferId)
  }

}
