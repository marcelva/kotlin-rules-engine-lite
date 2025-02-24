package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule

class LastNamePrefixRule(private val prefix: String) : Rule {
    override fun evaluate(person: Person): Boolean {
        println("Evaluating ${this::class.simpleName} for person $person... ")
        return person.lastName.startsWith(prefix, ignoreCase = true)
    }
}