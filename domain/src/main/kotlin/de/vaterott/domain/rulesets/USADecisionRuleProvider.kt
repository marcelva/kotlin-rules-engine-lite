package de.vaterott.domain.rulesets

import de.vaterott.api.Country
import de.vaterott.api.CountryDecisionRuleProvider
import de.vaterott.api.Decision
import de.vaterott.api.RuleSet
import de.vaterott.api.ruleSet
import de.vaterott.domain.rules.MinAgeRule
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class USADecisionRuleProvider : CountryDecisionRuleProvider {
    private val decisionRules: Map<Decision, RuleSet> = mapOf(
        Decision.CAN_DRINK_BEER to ruleSet {
            rule(MinAgeRule(18))
        },
        Decision.CAN_DRIVE_RIGHT to ruleSet {
            rule(MinAgeRule(16))
        }
    )

    override fun supports(country: Country): Boolean = country == Country.USA

    override fun getRuleSet(decision: Decision): RuleSet =
        decisionRules[decision]
            ?: throw IllegalArgumentException("Decision $decision not supported for USA.")
}