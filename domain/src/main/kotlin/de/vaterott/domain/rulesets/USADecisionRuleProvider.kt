package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.korulite.CountryDecisionRuleProvider
import de.vaterott.api.domain.Decision
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.MinAgeRule
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class USADecisionRuleProvider : CountryDecisionRuleProvider {

    override fun supports(country: Country): Boolean = country == Country.USA

    // Todo Refactor rules to be more readable
    private val decisionRules: Map<Decision, RuleSet> = mapOf(
        Decision.CAN_DRINK_BEER to ruleSet {
            rule(MinAgeRule(18))
        },
        Decision.CAN_DRIVE_RIGHT to ruleSet {
            rule(MinAgeRule(16))
        }
    )

    override fun getRuleSet(decision: Decision): RuleSet =
        decisionRules[decision]
            ?: throw IllegalArgumentException("Decision $decision not supported for USA.")
}