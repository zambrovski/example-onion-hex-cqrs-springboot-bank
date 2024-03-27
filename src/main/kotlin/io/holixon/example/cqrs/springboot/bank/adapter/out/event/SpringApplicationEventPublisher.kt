package io.holixon.example.cqrs.springboot.bank.adapter.out.event

import io.holixon.example.cqrs.springboot.bank.application.port.out.event.EventPublisherOutPort
import org.springframework.context.ApplicationEventPublisher

class SpringApplicationEventPublisher(
  private val applicationEventPublisher: ApplicationEventPublisher
) : EventPublisherOutPort {
  override fun publishEvent(event: Any) {
    applicationEventPublisher.publishEvent(event)
  }
}
