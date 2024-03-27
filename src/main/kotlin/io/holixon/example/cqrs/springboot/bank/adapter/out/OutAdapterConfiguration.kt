package io.holixon.example.cqrs.springboot.bank.adapter.out

import io.holixon.example.cqrs.springboot.bank.adapter.out.command.InMemBankAccountAggregateRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.adapter.out.command.InMemMoneyTransferAggregateRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.adapter.out.event.SpringApplicationEventPublisher
import io.holixon.example.cqrs.springboot.bank.adapter.out.query.InMemBankAccountCurrentBalanceRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.adapter.out.query.InMemMoneyTransferSummaryRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.event.EventPublisherOutPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OutAdapterConfiguration {

  @Bean
  fun bankAccountCurrentBalanceRepository(): BankAccountCurrentBalanceRepository = InMemBankAccountCurrentBalanceRepositoryImpl()

  @Bean
  fun moneyTransferSummaryRepository(): MoneyTransferSummaryRepository = InMemMoneyTransferSummaryRepositoryImpl()

  @Bean
  fun bankAccountRepository(): BankAccountAggregateRepository = InMemBankAccountAggregateRepositoryImpl()

  @Bean
  fun moneyTransferRepository(): MoneyTransferAggregateRepository = InMemMoneyTransferAggregateRepositoryImpl()

  @Bean
  fun eventPublisher(applicationEventPublisher: ApplicationEventPublisher): EventPublisherOutPort = SpringApplicationEventPublisher(applicationEventPublisher = applicationEventPublisher)
}
