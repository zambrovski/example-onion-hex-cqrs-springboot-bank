package io.holixon.example.cqrs.springboot.bank.infrastructure

import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferIdGenerator
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.event.EventPublisherOutPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import io.holixon.example.cqrs.springboot.bank.application.projector.BankAccountCurrentBalanceProjector
import io.holixon.example.cqrs.springboot.bank.application.projector.MoneyTransferSummaryProjector
import io.holixon.example.cqrs.springboot.bank.application.usecase.*
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
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
  fun createBankAccountUseCase(bankAccountRepository: BankAccountAggregateRepository, applicationEventPublisher: EventPublisherOutPort) =
    CreateBankAccountUseCase(
      bankAccountRepository = bankAccountRepository,
      applicationEventPublisher = applicationEventPublisher
    )

  @Bean
  fun depositMoneyUseCase(bankAccountRepository: BankAccountAggregateRepository, applicationEventPublisher: EventPublisherOutPort) =
    DepositMoneyUseCase(
      bankAccountRepository = bankAccountRepository,
      applicationEventPublisher = applicationEventPublisher
    )

  @Bean
  fun retrieveAccountInformationUseCase(bankAccountCurrentBalanceRepository: BankAccountCurrentBalanceRepository) =
    RetrieveAccountInformationUseCase(bankAccountCurrentBalanceRepository = bankAccountCurrentBalanceRepository)

  @Bean
  fun withdrawMoneyUseCase(bankAccountAggregateRepository: BankAccountAggregateRepository, applicationEventPublisher: EventPublisherOutPort) = WithdrawMoneyUseCase(
    bankAccountRepository = bankAccountAggregateRepository,
    applicationEventPublisher = applicationEventPublisher
  )


  @Bean
  fun transferMoneyUseCase(
    bankAccountAggregateRepository: BankAccountAggregateRepository,
    moneyTransferAggregateRepository: MoneyTransferAggregateRepository,
    moneyTransferIdGenerator: MoneyTransferIdGenerator,
    applicationEventPublisher: EventPublisherOutPort
  ) = TransferMoneyUseCase(
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
