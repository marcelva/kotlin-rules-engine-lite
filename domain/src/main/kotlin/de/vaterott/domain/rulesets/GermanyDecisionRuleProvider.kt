package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision
import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.DecisionRuleProvider
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.IsSameDayRule
import de.vaterott.domain.rules.LastNamePrefixRule
import de.vaterott.domain.rules.MinAgeRule
import jakarta.enterprise.context.ApplicationScoped
import java.time.DayOfWeek
import java.time.LocalDate

@ApplicationScoped
class GermanyDecisionRuleProvider: DecisionRuleProvider<Person> {

    override fun supports(enum: String): Boolean = enum == Country.DE.name

    private val decisionRules: Map<String, RuleSet<Person>> = mapOf(
        Decision.CAN_DRINK_BEER.name to ruleSet {
            rule(MinAgeRule(16))
            rule(LastNamePrefixRule("S"))
        },
        Decision.CAN_DRIVE_RIGHT.name to ruleSet {
            rule(MinAgeRule(18))
            // Soll nur heute auf der rechten Seite fahren dürfen
            rule(IsSameDayRule(LocalDate.now().dayOfWeek))
        }
    )

    override fun getRuleSet(decision: String): RuleSet<Person> {
        return decisionRules[decision]
            ?: throw IllegalArgumentException("Decision $decision not supported for USA.")
    }
}