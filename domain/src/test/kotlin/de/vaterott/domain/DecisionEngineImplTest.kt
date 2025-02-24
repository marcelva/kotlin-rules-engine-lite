package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision
import de.vaterott.api.domain.Person
import org.junit.jupiter.api.Test
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat

@QuarkusTest
class DecisionEngineImplTest {

    @Inject
    lateinit var decisionEngineImpl: DecisionEngineImpl

    @Test
    fun test() {
        val result = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER, person = Person(age = 23, country = Country.DE, lastName = "Smith"))
        assertThat(result).isTrue()
    }

}
