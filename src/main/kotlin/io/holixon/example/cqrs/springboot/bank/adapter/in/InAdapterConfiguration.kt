package io.holixon.example.cqrs.springboot.bank.adapter.`in`

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.projectmapk.jackson.module.kogera.KotlinModule
import io.holixon.example.cqrs.springboot.bank.domain.type.account.InsufficientBalance
import io.holixon.example.cqrs.springboot.bank.domain.type.account.MaximumBalanceExceeded
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferNotFound
import io.holixon.example.cqrs.springboot.bank.domain.type.moneytransfer.MoneyTransferStatus
import io.holixon.example.cqrs.springboot.bank.infrastructure.KotlinTypeInfo
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.jmolecules.ddd.annotation.ValueObject
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Configuration
@ControllerAdvice
@OpenAPIDefinition(
  info = Info(
    title = "CQRS Hex SpringBoot HoliBank",
    description = "Example implementation of a simple bank application refining Clean Architecture, CQRS and SpringBoot Framework."
  )
)
@ComponentScan
class InAdapterConfiguration {

  @Bean
  fun defaultOMCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
    return Jackson2ObjectMapperBuilderCustomizer { builder ->
      builder
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        .modules(
          KotlinModule.Builder().build(),
          JavaTimeModule()
        )
        .featuresToDisable(
          SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
          DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        )
        .mixIn(MoneyTransferStatus::class.java, KotlinTypeInfo::class.java)
    }
  }

  @ExceptionHandler(
    IllegalArgumentException::class,
    MaximumBalanceExceeded::class,
    InsufficientBalance::class
  )
  fun badRequest(exception: Exception): ResponseEntity<ErrorDto> {
    return ResponseEntity.badRequest().body(
      ErrorDto(exception.message ?: "")
    )
  }

  @ExceptionHandler(
    MoneyTransferNotFound::class,
  )
  fun notFound(exception: Exception): ResponseEntity<ErrorDto> {
    return ResponseEntity.notFound().build()
  }

  /**
   * Error DTO to return message details to the client.
   */
  @ValueObject
  data class ErrorDto(
    val message: String
  )
}
