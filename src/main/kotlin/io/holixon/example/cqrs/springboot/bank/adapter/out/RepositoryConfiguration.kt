package io.holixon.example.cqrs.springboot.bank.adapter.out

import io.holixon.example.cqrs.springboot.bank.adapter.out.command.InMemBankAccountAggregateRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.adapter.out.command.InMemMoneyTransferAggregateRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.adapter.out.query.BankAccountCurrentBalanceRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.adapter.out.query.MoneyTransferSummaryRepositoryImpl
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.BankAccountAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferAggregateRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.BankAccountCurrentBalanceRepository
import io.holixon.example.cqrs.springboot.bank.application.port.out.query.MoneyTransferSummaryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration {

  @Bean
  fun bankAccountCurrentBalanceRepository(): BankAccountCurrentBalanceRepository = BankAccountCurrentBalanceRepositoryImpl()

  @Bean
  fun moneyTransferSummaryRepository(): MoneyTransferSummaryRepository = MoneyTransferSummaryRepositoryImpl()

  @Bean
  fun bankAccountRepository(): BankAccountAggregateRepository = InMemBankAccountAggregateRepositoryImpl()

  @Bean
  fun moneyTransferRepository(): MoneyTransferAggregateRepository = InMemMoneyTransferAggregateRepositoryImpl()
}
