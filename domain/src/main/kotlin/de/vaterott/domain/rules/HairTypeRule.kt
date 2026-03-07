package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule
import de.vaterott.api.korulite.RuleDoc

@RuleDoc("Haartyp ist {expectedHairType}")
class HairTypeRule(private val expectedHairType: String) : Rule<Person> {
    override fun evaluate(fact: Person): Boolean = fact.hairType.equals(expectedHairType, ignoreCase = true)
}
