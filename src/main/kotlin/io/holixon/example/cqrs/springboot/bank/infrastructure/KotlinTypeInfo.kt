package io.holixon.example.cqrs.springboot.bank.infrastructure

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Type info for all classes using inheritance.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", include = JsonTypeInfo.As.PROPERTY)
class KotlinTypeInfo
