package de.vaterott.domain.rules

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.Rule
import java.time.DayOfWeek
import java.time.LocalDate

class IsSameDayRule(private val dow: DayOfWeek): Rule<Person> {

    override fun evaluate(person: Person): Boolean {
        return LocalDate.now().dayOfWeek == dow
    }

}