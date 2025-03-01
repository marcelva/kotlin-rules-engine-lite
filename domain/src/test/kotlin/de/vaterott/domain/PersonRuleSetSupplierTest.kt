package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.PersonAction
import de.vaterott.api.domain.Person
import org.junit.jupiter.api.Test
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

@QuarkusTest
class PersonRuleSetSupplierTest {

    @Inject
    lateinit var decisionEngineImpl: PersonRuleSetSupplier

    val mrSmith = Person(age = 19, country = Country.USA, lastName = "Mank")
    val mrAdalbert = Person(age = 23, country = Country.DE, lastName = "Adalbert")

    @Test
    fun canDrinkBeer() {
        val isMrSmithAllowedToDrink = decisionEngineImpl.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = mrSmith)
        assertThat(isMrSmithAllowedToDrink).describedAs("Mr Smith soll trinken dürfen").isTrue()

        val isMrAdalbertAllowedToDrink = decisionEngineImpl.evaluate(PersonAction.CAN_DRINK_BEER.name, fact= mrAdalbert)
        assertThat(isMrAdalbertAllowedToDrink).describedAs("Mr Adalbert soll nicht trinken dürfen").isFalse()
    }

    @Test
    fun canDriveRight() {
        val mrSmith = decisionEngineImpl.evaluate(PersonAction.CAN_DRIVE_RIGHT.name, fact = mrSmith)
        assertThat(mrSmith).describedAs("Mr Smith sollte auf der rechten Seiten der Straße fahren können").isFalse()

        val mrAdalbert = decisionEngineImpl.evaluate(PersonAction.CAN_DRIVE_RIGHT.name, fact = mrAdalbert)
        assertThat(mrAdalbert).describedAs("Mr Adalbert sollte auf der rechten Seiten der Straße fahren können")
            .isTrue()
    }

    @Test
    fun wrongDecision() {
        assertThrows<IllegalArgumentException>{
            decisionEngineImpl.evaluate("blub", fact = mrSmith)
        }
    }

}
