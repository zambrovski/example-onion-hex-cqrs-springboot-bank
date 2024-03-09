package io.holixon.example.cqrs.springboot.bank.application.port.out

import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferId
import org.jmolecules.architecture.hexagonal.SecondaryPort
import java.util.function.Supplier

/**
 * Supplier for money transfer ids.
 */
@SecondaryPort
fun interface MoneyTransferIdGenerator : Supplier<MoneyTransferId>
