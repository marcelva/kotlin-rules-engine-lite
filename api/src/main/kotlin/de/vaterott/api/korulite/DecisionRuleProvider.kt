package de.vaterott.api.korulite

interface DecisionRuleProvider<T> {
    fun supports(enum: T): Boolean
    fun getRuleSet(decision: String): RuleSet
}