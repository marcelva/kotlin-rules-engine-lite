package de.vaterott.domain.rules

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import org.junit.jupiter.api.Test
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat


class IsSameDayRuleTest {

    @Test
    fun isSameDayTest() {
        val notSameDay = IsSameDayRule(LocalDate.now().dayOfWeek.minus(1))
        assertThat(notSameDay.evaluate(Person(12, Country.USA, "Heinz"))).isFalse()

        val sameDay = IsSameDayRule(LocalDate.now().dayOfWeek)
        assertThat(sameDay.evaluate(Person(12, Country.USA, "Heinz"))).isTrue()
    }

}