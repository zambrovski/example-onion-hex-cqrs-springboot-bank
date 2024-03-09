package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.WithdrawMoneyInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.InsufficientBalance
import org.jmolecules.architecture.cqrs.CommandDispatcher
import org.springframework.context.ApplicationEventPublisher
import java.util.concurrent.CompletableFuture

/**
 * Use case to withdraw money.
 */
class WithdrawMoneyUseCase(
  private val bankAccountRepository: BankAccountAggregateRepository,
  private val applicationEventPublisher: ApplicationEventPublisher
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
      io.holixon.example.cqrs.springboot.bank.domain.command.api.WithdrawMoneyCommand(
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
