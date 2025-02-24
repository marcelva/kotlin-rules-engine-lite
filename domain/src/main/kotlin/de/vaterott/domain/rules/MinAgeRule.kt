package de.vaterott.domain.rules

import de.vaterott.api.Person
import de.vaterott.api.Rule

class MinAgeRule(private val minAge: Int) : Rule {
    override fun evaluate(person: Person): Boolean = person.age >= minAge
}

