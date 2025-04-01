package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule

class MinAgeRule(private val minAge: Int) : Rule<Person> {
    override fun evaluate(person: Person): Boolean {
        return person.age >= minAge
    }

    override fun toString(): String {
        return "MinAgeRule(minAge=$minAge)"
    }


}

