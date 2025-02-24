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

    val mrSmith = Person(age = 17, country = Country.DE, lastName = "Smith")
    val mrAdalbert = Person(age = 23, country = Country.DE, lastName = "Adalbert")

    @Test
    fun germanGuyWith() {
        val isMrSmithAllowedToDrink = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER, person = mrSmith)
        assertThat(isMrSmithAllowedToDrink).isTrue()

        val isMrAdalbertAllowedToDrink = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER, person = mrAdalbert)
        assertThat(isMrAdalbertAllowedToDrink).isFalse()

        val isMrSmithAllowedToDrinkInUSA = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER, person = mrSmith.copy(country = Country.USA))
        assertThat(isMrSmithAllowedToDrinkInUSA).isFalse()
    }

}
