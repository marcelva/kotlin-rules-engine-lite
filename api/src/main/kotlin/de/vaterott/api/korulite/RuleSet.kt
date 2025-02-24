package de.vaterott.api.korulite

import de.vaterott.api.domain.Person

class RuleSet(val rules: List<Rule>) {
    fun evaluate(person: Person): Boolean = rules.all { it.evaluate(person) }
}