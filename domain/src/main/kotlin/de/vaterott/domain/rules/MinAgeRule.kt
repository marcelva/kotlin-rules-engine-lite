package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule

class MinAgeRule(private val minAge: Int) : Rule {
    override fun evaluate(person: Person): Boolean {
        println("Evaluating ${this::class.simpleName} for person $person... ")
        return person.age >= minAge
    }


}

