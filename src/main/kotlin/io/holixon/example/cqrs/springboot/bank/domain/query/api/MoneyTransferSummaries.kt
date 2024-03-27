package io.holixon.example.cqrs.springboot.bank.domain.query.api

/**
 * Summary of money transfers.
 */
data class MoneyTransferSummaries(
  val elements: List<MoneyTransferSummary>
)
