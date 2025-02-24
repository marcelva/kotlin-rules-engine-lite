package de.vaterott.domain.rules

import de.vaterott.api.Person
import de.vaterott.api.Rule

class LastNamePrefixRule(private val prefix: String) : Rule {
    override fun evaluate(person: Person): Boolean =
        person.lastName.startsWith(prefix, ignoreCase = true)
}