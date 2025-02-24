package de.vaterott.api

interface CountryDecisionRuleProvider {
    fun supports(country: Country): Boolean
    fun getRuleSet(decision: Decision): RuleSet
}