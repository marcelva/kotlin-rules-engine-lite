package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule
import de.vaterott.api.korulite.RuleDoc

@RuleDoc("Augenfarbe ist {expectedEyeColor}")
class EyeColorRule(private val expectedEyeColor: String) : Rule<Person> {
    override fun evaluate(fact: Person): Boolean = fact.eyeColor.equals(expectedEyeColor, ignoreCase = true)
}
