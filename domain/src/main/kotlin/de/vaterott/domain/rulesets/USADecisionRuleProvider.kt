package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision
import de.vaterott.api.korulite.DecisionRuleProvider
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.MinAgeRule
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class USADecisionRuleProvider : DecisionRuleProvider<Country> {

    override fun supports(enum: Country): Boolean = enum == Country.USA

    // Todo Refactor rules to be more readable
    private val decisionRules: Map<String, RuleSet> = mapOf(
        Decision.CAN_DRINK_BEER.name to ruleSet {
            rule(MinAgeRule(18))
        },
        Decision.CAN_DRIVE_RIGHT.name to ruleSet {
            rule(MinAgeRule(16))
        }
    )

    override fun getRuleSet(decision: String): RuleSet =
        decisionRules[decision]
            ?: throw IllegalArgumentException("Decision $decision not supported for USA.")
}