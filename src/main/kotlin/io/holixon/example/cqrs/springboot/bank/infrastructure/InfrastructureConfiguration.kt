@file: InfrastructureRing

package io.holixon.example.cqrs.springboot.bank.infrastructure

import io.holixon.example.cqrs.springboot.bank.adapter.`in`.InAdapterConfiguration
import io.holixon.example.cqrs.springboot.bank.adapter.out.OutAdapterConfiguration
import org.jmolecules.architecture.onion.classical.InfrastructureRing
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.ApplicationEventMulticaster
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.core.task.SimpleAsyncTaskExecutor


@Configuration
@Import(
  InAdapterConfiguration::class,
  OutAdapterConfiguration::class
)
class InfrastructureConfiguration {

  @Bean(name = ["applicationEventMulticaster"])
  fun simpleApplicationEventMulticaster(): ApplicationEventMulticaster {
    val eventMulticaster = SimpleApplicationEventMulticaster()
    eventMulticaster.setTaskExecutor(SimpleAsyncTaskExecutor())
    return eventMulticaster
  }
}
