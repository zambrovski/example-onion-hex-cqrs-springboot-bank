package io.holixon.example.cqrs.springboot.bank.application.port.out.event

interface EventPublisherOutPort {
  fun publishEvent(event: Any)
}
