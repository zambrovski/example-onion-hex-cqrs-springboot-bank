package io.holixon.example.cqrs.springboot.bank.domain.type.account

/**
 * Bank account current balance.
 */
data class CurrentBalance(
        val accountId: AccountId,
        val currentBalance: Balance
)
