package io.holixon.example.cqrs.springboot.bank.application

/*
import mu.KLogging
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.jmolecules.event.annotation.DomainEventHandler
import org.jmolecules.event.annotation.DomainEventPublisher
import org.jmolecules.example.axonframework.bank.domain.moneytransfer.event.*
import org.jmolecules.example.axonframework.bank.domain.moneytransfer.command.MoneyTransfer
import org.jmolecules.example.axonframework.bank.domain.moneytransfer.type.RejectionReason
import io.holixon.example.cqrs.springboot.bank.application.port.out.command.MoneyTransferCommandPort
import io.holixon.example.cqrs.springboot.bank.application.port.out.event.MoneyTransferEventPort

@Saga
class MoneyTransferProcessManager {

  companion object : KLogging()

  private lateinit var moneyTransfer: MoneyTransfer

  @StartSaga
  @SagaEventHandler(associationProperty = "moneyTransferId")
  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferRequestedEvent")
  fun on(evt: MoneyTransferRequestedEvent, moneyTransferCommandPort: MoneyTransferCommandPort) {
    logger.info { "MoneyTransfer [${evt.moneyTransferId}] requested." }
    moneyTransfer = MoneyTransfer(
      moneyTransferId = evt.moneyTransferId,
      sourceAccountId = evt.sourceAccountId,
      targetAccountId = evt.targetAccountId,
      amount = evt.amount
    )
    try {
      moneyTransferCommandPort.receiveMoneyTransfer(moneyTransfer)
    } catch (e: Exception) {
      moneyTransferCommandPort.cancelMoneyTransfer(moneyTransfer, RejectionReason.of(e.message))
    }
  }

  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferReceivedEvent")
  @SagaEventHandler(associationProperty = "moneyTransferId")
  fun on(evt: MoneyTransferReceivedEvent, moneyTransferCommandPort: MoneyTransferCommandPort) {
    logger.trace { "on($evt)" }
    try {
      moneyTransferCommandPort.completeMoneyTransfer(moneyTransfer)
    } catch (e: Exception) {
      moneyTransferCommandPort.cancelMoneyTransfer(moneyTransfer, RejectionReason.of(e.message))
    }
  }

  @SagaEventHandler(associationProperty = "moneyTransferId")
  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferCompletedEvent")
  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyTransferredEvent",
    type = DomainEventPublisher.PublisherType.EXTERNAL
  )
  @EndSaga
  fun on(evt: MoneyTransferCompletedEvent, moneyTransferEventPort: MoneyTransferEventPort) {
    logger.trace { "on($evt)" }
    logger.info { "MoneyTransfer [${evt.moneyTransferId}] succeeded." }
    moneyTransferEventPort.moneyTransferSucceeded(
      io.holixon.example.cqrs.springboot.bank.domain.event.moneytransfer.MoneyTransferredEvent(
        moneyTransferId = moneyTransfer.moneyTransferId,
        sourceAccountId = moneyTransfer.sourceAccountId,
        targetAccountId = moneyTransfer.targetAccountId,
        amount = moneyTransfer.amount
      )
    )
  }

  @SagaEventHandler(associationProperty = "moneyTransferId")
  @DomainEventHandler(namespace = "springboot.bank", name = "MoneyTransferCancelledEvent")
  @DomainEventPublisher(
    publishes = "springboot.bank.MoneyTransferFailedEvent",
    type = DomainEventPublisher.PublisherType.EXTERNAL
  )
  @EndSaga
  fun on(evt: MoneyTransferCancelledEvent, moneyTransferEventPort: MoneyTransferEventPort) {
    logger.trace { "on($evt)" }
    logger.info { "MoneyTransfer [${evt.moneyTransferId}] failed." }
    moneyTransferEventPort.moneyTransferFailed(
      MoneyTransferFailedEvent(
        moneyTransferId = moneyTransfer.moneyTransferId,
        sourceAccountId = moneyTransfer.sourceAccountId,
        targetAccountId = moneyTransfer.targetAccountId,
        amount = moneyTransfer.amount,
        reason = evt.reason
      )
    )
  }

  override fun toString(): String {
    return moneyTransfer.toString()
  }
}


 */
