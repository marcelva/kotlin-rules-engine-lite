package de.vaterott.api.korulite

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision

// Todo Generalize DecisionRuleProvider to be independent from "country" attribute
interface CountryDecisionRuleProvider {
    fun supports(country: Country): Boolean
    fun getRuleSet(decision: Decision): RuleSet
}