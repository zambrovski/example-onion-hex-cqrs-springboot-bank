package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.WithdrawMoneyInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.event.EventPublisherOutPort
import io.holixon.example.cqrs.springboot.bank.domain.command.api.atm.WithdrawMoneyCommand
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import org.jmolecules.architecture.cqrs.CommandDispatcher
import java.util.concurrent.CompletableFuture

/**
 * Use case to withdraw money.
 */
class WithdrawMoneyUseCase(
  private val bankAccountRepository: BankAccountAggregateRepository,
  private val applicationEventPublisher: EventPublisherOutPort
) : WithdrawMoneyInPort {

  /**
   * Withdraws money from given account.
   * @param accountId account id.
   * @param amount amount to withdraw.
   */
  @Throws(InsufficientBalance::class)
  @CommandDispatcher(dispatches = "springboot.bank.WithdrawMoneyCommand")
  override fun withdrawMoney(accountId: AccountId, amount: Amount): CompletableFuture<Unit> {

    // load
    val bankAccount = bankAccountRepository.load(accountId = accountId)

    // perform
    val event = bankAccount.handle(
      WithdrawMoneyCommand(
        accountId,
        amount
      )
    )

    // save
    bankAccountRepository.save(bankAccount)

    // notify
    applicationEventPublisher.publishEvent(event)

    return CompletableFuture.completedFuture(Unit)
  }

}
