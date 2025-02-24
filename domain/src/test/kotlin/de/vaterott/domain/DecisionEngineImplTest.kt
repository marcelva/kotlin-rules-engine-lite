package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision
import de.vaterott.api.domain.Person
import org.junit.jupiter.api.Test
import javax.inject.Inject
import io.quarkus.test.component.QuarkusComponentTest
import org.assertj.core.api.Assertions.assertThat

@QuarkusComponentTest
class DecisionEngineImplTest {

    @Inject
    lateinit var decisionEngineImpl: DecisionEngineImpl

    @Test
    fun test() {
        val result = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER, person = Person(age = 23, country = Country.DE, lastName = "Smith"))
        assertThat(result).isTrue()
    }

}
