package de.vaterott.api

class RuleSet(val rules: List<Rule>) {
    fun evaluate(person: Person): Boolean = rules.all { it.evaluate(person) }
}