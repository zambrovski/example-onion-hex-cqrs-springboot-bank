package io.holixon.example.cqrs.springboot.bank.domain.command.model.account

import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.holixon.example.cqrs.springboot.bank.domain.type.account.ReservedAmount
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId

/**
 * Represents money transfers of a bank account.
 */
data class ActiveMoneyTransfers(
  private val activeMoneyTransfers: MutableMap<MoneyTransferId, Amount> = mutableMapOf()
) {
  fun getReservedAmount(): ReservedAmount = ReservedAmount.of(activeMoneyTransfers.values.sumOf { it.value })

  fun getAmountForTransfer(moneyTransferId: MoneyTransferId): Amount? = activeMoneyTransfers[moneyTransferId]

  fun hasMoneyTransfer(moneyTransferId: MoneyTransferId): Boolean = activeMoneyTransfers.containsKey(moneyTransferId)

  fun initTransfer(moneyTransferId: MoneyTransferId, amount: Amount) {
    activeMoneyTransfers[moneyTransferId] = amount
  }

  fun completeTransfer(moneyTransferId: MoneyTransferId) {
    activeMoneyTransfers.remove(moneyTransferId)
  }

  fun cancelTransfer(moneyTransferId: MoneyTransferId) {
    activeMoneyTransfers.remove(moneyTransferId)
  }
}
