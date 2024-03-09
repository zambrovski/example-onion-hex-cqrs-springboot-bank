package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.CreateBankAccountInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.domain.command.model.BankAccountAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Balance
import io.holixon.example.cqrs.springboot.bank.domain.type.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.MaximumBalanceExceeded
import org.jmolecules.architecture.cqrs.CommandDispatcher
import org.springframework.context.ApplicationEventPublisher
import java.util.concurrent.CompletableFuture

/**
 * Application Service to manipulate the bank account.
 */
class CreateBankAccountUseCase(
  private val bankAccountRepository: BankAccountAggregateRepository,
  private val applicationEventPublisher: ApplicationEventPublisher
) : CreateBankAccountInPort {

  /**
   * Creates a bank account.
   * @param accountId account id.
   * @param initialBalance balance of the account.
   */
  @Throws(MaximumBalanceExceeded::class, InsufficientBalance::class)
  @CommandDispatcher(dispatches = "springboot.bank.CreateBankAccountCommand")
  override fun createBankAccount(accountId: AccountId, initialBalance: Balance): CompletableFuture<Unit> {

    // create
    val (bankAccount, event) = BankAccountAggregate.handle(
      io.holixon.example.cqrs.springboot.bank.domain.command.api.CreateBankAccountCommand(
        accountId = accountId,
        initialBalance = initialBalance
      )
    )

    // store the aggregate
    bankAccountRepository.save(bankAccount = bankAccount)

    // notify
    applicationEventPublisher.publishEvent(event)

    return CompletableFuture.completedFuture(Unit)
  }
}
