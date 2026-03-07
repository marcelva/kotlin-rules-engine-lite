package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule
import de.vaterott.api.korulite.RuleDoc

@RuleDoc("Nachname beginnt mit '{prefix}'")
class LastNamePrefixRule(private val prefix: String) : Rule<Person> {
    override fun evaluate(person: Person): Boolean {
        return person.lastName.startsWith(prefix, ignoreCase = true)
    }

    override fun toString(): String {
        return "LastNamePrefixRule(prefix='$prefix')"
    }


}