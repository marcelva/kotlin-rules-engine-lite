package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule

class HairTypeRule(private val expectedHairType: String) : Rule<Person> {
    override fun evaluate(fact: Person): Boolean = fact.hairType.equals(expectedHairType, ignoreCase = true)
}
