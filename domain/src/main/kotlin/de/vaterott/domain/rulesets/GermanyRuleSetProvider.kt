package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.PersonAction
import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.RuleSetProvider
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.IsSameDayRule
import de.vaterott.domain.rules.LastNamePrefixRule
import de.vaterott.domain.rules.MinAgeRule
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class GermanyRuleSetProvider: RuleSetProvider<Person> {

    override fun supports(filterBy: String): Boolean = filterBy == Country.DE.name

    private val personActionRules: Map<String, RuleSet<Person>> = mapOf(
        PersonAction.CAN_DRINK_BEER.name to ruleSet {
            rule(MinAgeRule(16))
            rule(LastNamePrefixRule("S"))
        },
        PersonAction.CAN_DRIVE_RIGHT.name to ruleSet {
            rule(MinAgeRule(18))
            // Soll nur heute auf der rechten Seite fahren dürfen
            rule(IsSameDayRule(LocalDate.now().dayOfWeek))
        }
    )

    override fun getRuleSet(action: String): RuleSet<Person> {
        return personActionRules[action]
            ?: throw IllegalArgumentException("Decision $action not supported for USA.")
    }
}