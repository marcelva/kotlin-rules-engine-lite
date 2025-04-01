package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule

class SameAgeRule(private val age: Int) : Rule<Person> {
    override fun evaluate(person: Person): Boolean {
        return person.age == age
    }

    override fun toString(): String {
        return "SameAgeRule(age=$age)"
    }


}