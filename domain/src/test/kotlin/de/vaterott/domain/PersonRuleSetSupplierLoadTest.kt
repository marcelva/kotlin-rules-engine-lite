package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import de.vaterott.api.domain.PersonAction
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import java.util.concurrent.TimeUnit


@QuarkusTest
@Tag("performance")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRuleSetSupplierLoadTest {

    @Inject
    lateinit var personRuleSetSupplier: PersonRuleSetSupplier

    private var startTime: Long = 0L
    private val repetitions: Int =
        System.getProperty("loadTest.repetitions")?.toIntOrNull()?.coerceAtLeast(1) ?: 1000

    @BeforeAll
    fun beforeAll() {
        startTime = System.nanoTime()
    }

    @Test
    fun repeatedTest() {
        repeat(repetitions) {
            personRuleSetSupplier.evaluate(
                PersonAction.CAN_DRINK_BEER.name,
                fact = Person(lastName = "Stefan", age = 18, country = Country.DE)
            )
        }
    }

    @RepeatedTest(10)
    fun dontRepeatTest(testInfo: TestInfo) {
        personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = Person(lastName = "${testInfo.displayName}", age = 18, country = Country.DE))
    }

    @AfterAll
    fun afterAll() {
        val duration = System.nanoTime() - startTime
        assertThat(TimeUnit.NANOSECONDS.toMillis(duration))
            .describedAs { "You are wasting my time ;)" }
            .isLessThanOrEqualTo(10000)
        println("Complete time to call $repetitions evaluations: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms")
    }

}
