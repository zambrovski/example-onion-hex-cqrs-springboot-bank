package io.holixon.example.cqrs.springboot.bank.application.usecase

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.CreateBankAccountInPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.event.EventPublisherOutPort
import io.holixon.example.cqrs.springboot.bank.domain.command.api.account.CreateBankAccountCommand
import io.holixon.example.cqrs.springboot.bank.domain.command.model.BankAccountAggregate
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Balance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import org.jmolecules.architecture.cqrs.CommandDispatcher
import java.util.concurrent.CompletableFuture

/**
 * Application Service to manipulate the bank account.
 */
class CreateBankAccountUseCase(
  private val bankAccountRepository: BankAccountAggregateRepository,
  private val applicationEventPublisher: EventPublisherOutPort
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
      CreateBankAccountCommand(
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
