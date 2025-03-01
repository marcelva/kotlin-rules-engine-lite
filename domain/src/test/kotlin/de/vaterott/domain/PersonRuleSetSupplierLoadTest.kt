package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import de.vaterott.api.domain.PersonAction
import de.vaterott.domain.rulesets.GermanyRuleSetProvider
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectSpy
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit


@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRuleSetSupplierLoadTest {

    @Inject
    lateinit var personRuleSetSupplier: PersonRuleSetSupplier

    var startTime: Long = 0L

    @BeforeAll
    fun beforeAll() {
        startTime = System.nanoTime()
    }

    @RepeatedTest(10000)
    fun repeatedTest() {
        personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = Person(lastName = "Stefan",  age = 18, country = Country.DE))
    }

    @AfterAll
    fun afterAll() {
        val duration = System.nanoTime() - startTime
        assertThat(TimeUnit.NANOSECONDS.toMillis(duration))
            .describedAs { "You are wasting my time ;)" }
            .isLessThanOrEqualTo(10000)
        println("Complete time to call 10000 evualtions: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms")
    }

}