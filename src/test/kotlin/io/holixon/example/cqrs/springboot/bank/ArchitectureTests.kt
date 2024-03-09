package io.holixon.example.cqrs.springboot.bank

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchIgnore
import com.tngtech.archunit.junit.ArchTest
import org.jmolecules.archunit.ExtendedArchitectureRules
import org.junit.jupiter.api.Disabled

/**
 * Test compliance to hexagonal architecture based on JMolecules annotations.
 */
@ArchIgnore
@AnalyzeClasses(packages = ["io.holixon.example.cqrs.springboot.bank"])
internal class ArchitectureTests {

  @ArchTest
  fun `validates architecture is hexagonal`(classes: JavaClasses) {
    ExtendedArchitectureRules.ensureHexagonal(setOf("JMolecules")).check(classes)
  }

  @ArchTest
  fun `validates architecture is onion`(classes: JavaClasses) {
    ExtendedArchitectureRules.ensureOnionClassical(setOf("JMolecules")).check(classes)
  }

  @ArchTest
  fun `validates cqrs-es axon framework rules`(classes: JavaClasses) {
    ExtendedArchitectureRules.ensureAxonCqrsEs(setOf("JMolecules")).check(classes)
  }

}
