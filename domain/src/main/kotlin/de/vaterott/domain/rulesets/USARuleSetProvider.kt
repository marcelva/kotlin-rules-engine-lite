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
class USARuleSetProvider : RuleSetProvider<Person> {

    override fun supports(filterBy: String): Boolean = filterBy == Country.USA.name

    private val knowledgeBase: Map<String, RuleSet<Person>> = mapOf(
        PersonAction.CAN_DRINK_BEER.name to ruleSet {
            rule(MinAgeRule(18))
            rule(LastNamePrefixRule("M"))
        },
        PersonAction.CAN_DRIVE_RIGHT.name to ruleSet {
            rule(MinAgeRule(16))
            rule(IsSameDayRule(LocalDate.now().dayOfWeek.minus(1)))
        }
    )

    override fun getRuleSet(action: String): RuleSet<Person> {
        println("(USA) Checking for rule set for action: $action in $knowledgeBase")
        return knowledgeBase[action]
            ?: throw IllegalArgumentException("Action $action not supported for ${this::class.java.simpleName}.")
    }
}