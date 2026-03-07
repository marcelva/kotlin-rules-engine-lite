package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import de.vaterott.api.domain.PersonAction
import de.vaterott.api.korulite.ProviderDoc
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.RuleSetDoc
import de.vaterott.api.korulite.RuleSetProvider
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.IsSameDayRule
import de.vaterott.domain.rules.LastNamePrefixRule
import de.vaterott.domain.rules.MinAgeRule
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
@ProviderDoc("🇺🇸 USA")
class USARuleSetProvider : RuleSetProvider<Person> {

    override fun supports(filterBy: String): Boolean = filterBy == Country.USA.name

    @RuleSetDoc(
        action = "CAN_DRINK_BEER",
        title = "Bierfreigabe",
        summary = "US-spezifische Freigabe für Bierangebote"
    )
    private val canDrinkBeer = ruleSet {
        rule(MinAgeRule(18))
        rule(LastNamePrefixRule("M"))
    }

    @RuleSetDoc(
        action = "CAN_DRIVE_RIGHT",
        title = "Fahrtfreigabe",
        summary = "US-spezifisches Regelwerk für Fahrberechtigungen"
    )
    private val canDriveRight = ruleSet {
        rule(MinAgeRule(16))
        rule(IsSameDayRule(LocalDate.now().dayOfWeek))
    }

    private val knowledgeBase: Map<String, RuleSet<Person>> = mapOf(
        PersonAction.CAN_DRINK_BEER.name to canDrinkBeer,
        PersonAction.CAN_DRIVE_RIGHT.name to canDriveRight
    )

    override fun listRuleSets(): Map<String, RuleSet<Person>> = knowledgeBase

    override fun getRuleSet(action: String): RuleSet<Person> {
        return knowledgeBase[action]
            ?: throw IllegalArgumentException("Action $action not supported for ${this::class.java.simpleName}.")
    }
}
