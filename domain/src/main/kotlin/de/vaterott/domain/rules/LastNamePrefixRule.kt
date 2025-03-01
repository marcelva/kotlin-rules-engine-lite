package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule

class LastNamePrefixRule(private val prefix: String) : Rule<Person> {
    override fun evaluate(person: Person): Boolean {
        return person.lastName.startsWith(prefix, ignoreCase = true)
    }
}