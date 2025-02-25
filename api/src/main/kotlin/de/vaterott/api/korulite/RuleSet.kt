package de.vaterott.api.korulite


class RuleSet<T>(val rules: List<Rule<T>>) {
    fun evaluate(input: T): Boolean = rules.all { it.evaluate(input) }
}