package io.holixon.example.cqrs.springboot.bank.domain.query

import io.holixon.example.cqrs.springboot.bank.domain.type.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.Balance
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity

/**
 * Entity to store bank account current balance.
 */
@Entity
class BankAccountCurrentBalance(@Identity val accountId: AccountId, val balance: Balance) {
  fun decreaseBalance(amount: Amount): BankAccountCurrentBalance {
    return BankAccountCurrentBalance(accountId, balance - amount)
  }

  fun increaseBalance(amount: Amount): BankAccountCurrentBalance {
    return BankAccountCurrentBalance(accountId, balance + amount)
  }
}
