package io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer

/**
 * Summary of money transfers.
 */
data class MoneyTransferSummaries(
  val elements: List<MoneyTransferSummary>
)
