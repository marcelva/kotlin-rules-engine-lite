package de.vaterott.api.korulite


class RuleSet<T>(val rules: List<Rule<T>>) {
    fun evaluate(fact: T): Boolean = rules.all { it.evaluate(fact) }

    override fun toString(): String {
        return "RuleSet(rules=$rules)"
    }
}