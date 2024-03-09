package io.holixon.example.cqrs.springboot.bank.infrastructure

import io.holixon.example.cqrs.springboot.bank.application.port.out.MoneyTransferIdGenerator
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import io.holixon.example.cqrs.springboot.bank.application.projector.BankAccountCurrentBalanceProjector
import io.holixon.example.cqrs.springboot.bank.application.projector.MoneyTransferSummaryProjector
import io.holixon.example.cqrs.springboot.bank.application.usecase.*
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ComponentScan
class BankContextConfiguration {

  @Bean
  fun randomGenerator() = MoneyTransferIdGenerator { MoneyTransferId.of(UUID.randomUUID().toString()) }

  @Bean
  fun createBankAccountUseCase(bankAccountRepository: BankAccountAggregateRepository, applicationEventPublisher: ApplicationEventPublisher) =
    CreateBankAccountUseCase(
      bankAccountRepository = bankAccountRepository,
      applicationEventPublisher = applicationEventPublisher
    )

  @Bean
  fun depositMoneyUseCase(bankAccountRepository: BankAccountAggregateRepository, applicationEventPublisher: ApplicationEventPublisher) =
    DepositMoneyUseCase(
      bankAccountRepository = bankAccountRepository,
      applicationEventPublisher = applicationEventPublisher
    )

  @Bean
  fun retrieveAccountInformationUseCase(bankAccountCurrentBalanceRepository: BankAccountCurrentBalanceRepository) =
    RetrieveAccountInformationUseCase(bankAccountCurrentBalanceRepository = bankAccountCurrentBalanceRepository)

  @Bean
  fun withdrawMoneyUseCase(bankAccountAggregateRepository: BankAccountAggregateRepository, applicationEventPublisher: ApplicationEventPublisher) = WithdrawMoneyUseCase(
    bankAccountRepository = bankAccountAggregateRepository,
    applicationEventPublisher = applicationEventPublisher
  )


  @Bean
  fun transferMoneyUseCase(
    moneyTransferProcessManager: MoneyTransferProcessManager
  ) = TransferMoneyUseCase(
    moneyTransferProcessManager = moneyTransferProcessManager
  )

  @Bean
  fun moneyTransferProcessManager(
    bankAccountAggregateRepository: BankAccountAggregateRepository,
    moneyTransferAggregateRepository: MoneyTransferAggregateRepository,
    moneyTransferIdGenerator: MoneyTransferIdGenerator,
    applicationEventPublisher: ApplicationEventPublisher

  ) = MoneyTransferProcessManager(
    moneyTransferIdGenerator = moneyTransferIdGenerator,
    bankAccountRepository = bankAccountAggregateRepository,
    moneyTransferAggregateRepository = moneyTransferAggregateRepository,
    applicationEventPublisher = applicationEventPublisher
  )

  @Bean
  fun retrieveMoneyTransferInformationUseCase(
    moneyTransferSummaryRepository: MoneyTransferSummaryRepository
  ) = RetrieveMoneyTransferInformationUseCase(
    moneyTransferSummaryRepository = moneyTransferSummaryRepository
  )

  @Bean
  fun bankAccountCurrentBalanceProjector(repository: BankAccountCurrentBalanceRepository) = BankAccountCurrentBalanceProjector(
    repository = repository
  )

  @Bean
  fun moneyTransferSummaryProjectorProjector(repository: MoneyTransferSummaryRepository) = MoneyTransferSummaryProjector(
    repository = repository
  )
}
