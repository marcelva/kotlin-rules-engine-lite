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
    lateinit var personRuleSetSupplier: PersonRuleSetSupplier

    val mrSmith = Person(age = 19, country = Country.USA, lastName = "Mank")
    val mrAdalbert = Person(age = 23, country = Country.DE, lastName = "Adalbert")

    @Test
    fun canDrinkBeer() {
        val isMrSmithAllowedToDrink = personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = mrSmith)
        assertThat(isMrSmithAllowedToDrink).describedAs("Mr Smith soll trinken dürfen").isTrue()

        val isMrAdalbertAllowedToDrink = personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact= mrAdalbert)
        assertThat(isMrAdalbertAllowedToDrink).describedAs("Mr Adalbert soll nicht trinken dürfen").isFalse()
    }

    @Test
    fun canDriveRight() {
        val mrSmith = personRuleSetSupplier.evaluate(PersonAction.CAN_DRIVE_RIGHT.name, fact = mrSmith)
        assertThat(mrSmith).describedAs("Mr Smith sollte auf der rechten Seiten der Straße fahren können").isFalse()

        val mrAdalbert = personRuleSetSupplier.evaluate(PersonAction.CAN_DRIVE_RIGHT.name, fact = mrAdalbert)
        assertThat(mrAdalbert).describedAs("Mr Adalbert sollte auf der rechten Seiten der Straße fahren können")
            .isTrue()
    }

    @Test
    fun `combine two ruleSets with or`() {
        val mrOr = Person(age = 12, country = Country.LOGICALOP, lastName = "MrOr")
        val canMrOrDrinkBeerAtAge12 = personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = mrOr)
        assertThat(canMrOrDrinkBeerAtAge12).describedAs("Mr Or soll trinken dürfen").isTrue()

        val mrOrAge18 = Person(age = 18, country = Country.LOGICALOP, lastName = "MrOr")
        val canMrOrDrinkBeerAtAge18 = personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = mrOrAge18)
        assertThat(canMrOrDrinkBeerAtAge18).describedAs("Mr Or mit 18 sollte nicht trinken dürfen").isFalse()

        val orAge18 = Person(age = 18, country = Country.LOGICALOP, lastName = "Or")
        val canOrDrinkBeerAtAge18 = personRuleSetSupplier.evaluate(PersonAction.CAN_DRINK_BEER.name, fact = orAge18)
        assertThat(canOrDrinkBeerAtAge18).describedAs("Or mit 18 sollte  trinken dürfen").isTrue()
    }

    @Test
    fun wrongAction() {
        assertThrows<IllegalArgumentException>{
            personRuleSetSupplier.evaluate("blub", fact = mrSmith)
        }
    }

}
