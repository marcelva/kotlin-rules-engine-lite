package de.vaterott.api.korulite

interface DecisionRuleProvider<T> {
    fun supports(enum: String): Boolean
    fun getRuleSet(decision: String): RuleSet<T>
}