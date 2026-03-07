package de.vaterott.api.korulite

interface RuleSetProvider<T> {
    fun supports(filterBy: String): Boolean
    fun getRuleSet(action: String): RuleSet<T>
    fun listRuleSets(): Map<String, RuleSet<T>> = emptyMap()
}
