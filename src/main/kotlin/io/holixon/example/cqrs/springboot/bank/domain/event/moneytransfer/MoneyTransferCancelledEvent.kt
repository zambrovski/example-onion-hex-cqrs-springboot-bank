package io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer

import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.RejectionReason
import org.jmolecules.event.annotation.DomainEvent

@DomainEvent(namespace = "springboot.bank", name = "MoneyTransferCancelledEvent")
data class MoneyTransferCancelledEvent(
  val moneyTransferId: MoneyTransferId,
  val reason: RejectionReason
)
