package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision
import de.vaterott.api.domain.Person
import org.junit.jupiter.api.Test
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

@QuarkusTest
class DecisionEngineImplTest {

    @Inject
    lateinit var decisionEngineImpl: PersonDecisionEngine

    val mrSmith = Person(age = 19, country = Country.USA, lastName = "Mank")
    val mrAdalbert = Person(age = 23, country = Country.DE, lastName = "Adalbert")

    @Test
    fun canDrinkBeer() {
        val isMrSmithAllowedToDrink = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER.name, input = mrSmith)
        assertThat(isMrSmithAllowedToDrink).describedAs("Mr Smith soll trinken dürfen").isTrue()

        val isMrAdalbertAllowedToDrink = decisionEngineImpl.evaluate(Decision.CAN_DRINK_BEER.name, input= mrAdalbert)
        assertThat(isMrAdalbertAllowedToDrink).describedAs("Mr Adalbert soll nicht trinken dürfen").isFalse()
    }

    @Test
    fun canDriveRight() {
        val mrSmith = decisionEngineImpl.evaluate(Decision.CAN_DRIVE_RIGHT.name, input = mrSmith)
        assertThat(mrSmith).describedAs("Mr Smith sollte auf der rechten Seiten der Straße fahren können").isFalse()

        val mrAdalbert = decisionEngineImpl.evaluate(Decision.CAN_DRIVE_RIGHT.name, input = mrAdalbert)
        assertThat(mrAdalbert).describedAs("Mr Adalbert sollte auf der rechten Seiten der Straße fahren können")
            .isTrue()
    }

    @Test
    fun wrongDecision() {
        assertThrows<IllegalArgumentException>{
            decisionEngineImpl.evaluate("blub", input = mrSmith)
        }
    }

}
