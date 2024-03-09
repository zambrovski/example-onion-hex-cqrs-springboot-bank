package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.DepositMoneyInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.domain.command.model.BankAccountAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.MaximumBalanceExceeded
import org.jmolecules.architecture.cqrs.CommandDispatcher
import org.springframework.context.ApplicationEventPublisher
import java.util.concurrent.CompletableFuture

/**
 * Operations on bank account available at ATM.
 */
class DepositMoneyUseCase(
  private val bankAccountRepository: BankAccountAggregateRepository,
  private val applicationEventPublisher: ApplicationEventPublisher
) : DepositMoneyInPort {

  /**
   * Deposits money on the given account.
   * @param accountId account id.
   * @param amount amount to deposit.
   */
  @Throws(MaximumBalanceExceeded::class)
  @CommandDispatcher(dispatches = "springboot.bank.DepositMoneyCommand")
  override fun depositMoney(accountId: AccountId, amount: Amount): CompletableFuture<Unit> {

    // load
    val bankAccount: BankAccountAggregate = bankAccountRepository.load(accountId = accountId)

    // perform
    val event = bankAccount.handle(
      io.holixon.example.cqrs.springboot.bank.domain.command.api.DepositMoneyCommand(
        accountId,
        amount
      )
    )

    // store
    bankAccountRepository.save(bankAccount)

    // event
    applicationEventPublisher.publishEvent(event)

    return CompletableFuture.completedFuture(Unit)
  }

  private fun load(): BankAccountAggregate {
    TODO()
  }
}
