package io.holixon.example.cqrs.springboot.bank.adapter.`in`.rest

import io.holixon.example.cqrs.springboot.bank.application.port.`in`.DepositMoneyInPort
import io.holixon.example.cqrs.springboot.bank.application.port.`in`.WithdrawMoneyInPort
import io.holixon.example.cqrs.springboot.bank.domain.type.account.AccountId
import io.holixon.example.cqrs.springboot.bank.domain.type.account.Amount
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import mu.KLogging
import org.jmolecules.ddd.annotation.ValueObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * ATM Resource.
 */
@RestController
@RequestMapping("/rest/atm")
class AtmResource(
  private val withdrawMoneyInPort: WithdrawMoneyInPort,
  private val depositMoneyInPort: DepositMoneyInPort,
) {

  companion object : KLogging()

  @PutMapping(value = ["/withdraw-money"])
  @Operation(
    summary = "Withdraws money from account.",
    responses = [
      ApiResponse(responseCode = "204", description = "Successful response."),
      ApiResponse(responseCode = "400", description = "Insufficient balance."),
      ApiResponse(responseCode = "404", description = "Account not found.")
    ]
  )
  fun withdrawMoney(@RequestBody dto: WithdrawMoneyDto): ResponseEntity<Void> {
    logger.info { "ATM Withdraw Request: $dto" }
    withdrawMoneyInPort.withdrawMoney(AccountId.of(dto.accountId), Amount.of(dto.amount)).get()
    return ResponseEntity.noContent().build()
  }

  @PutMapping("/deposit-money")
  @Operation(
    summary = "Deposits money to account.",
    responses = [
      ApiResponse(responseCode = "204", description = "Successful response."),
      ApiResponse(responseCode = "400", description = "Maximum balance exceeded."),
      ApiResponse(responseCode = "404", description = "Account not found.")
    ]
  )
  fun depositMoney(@RequestBody dto: DepositMoneyDto): ResponseEntity<Void> {
    logger.info { "ATM Deposit Request: $dto" }
    depositMoneyInPort.depositMoney(AccountId.of(dto.accountId), Amount.of(dto.amount)).get()
    return ResponseEntity.noContent().build()
  }

  @ValueObject
  data class DepositMoneyDto(
    val accountId: String,
    val amount: Int
  )

  @ValueObject
  data class WithdrawMoneyDto(
    val accountId: String,
    val amount: Int
  )
}
