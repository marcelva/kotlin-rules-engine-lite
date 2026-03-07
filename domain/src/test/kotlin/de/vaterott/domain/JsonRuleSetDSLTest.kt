package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.JsonRuleSetDSL
import de.vaterott.domain.rules.EyeColorRule
import de.vaterott.domain.rules.HairTypeRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JsonRuleSetDSLTest {

    @Test
    fun `can define a person rule set via json`() {
        val json =
            """
            {
              "ruleSets": [
                {
                  "name": "Ruleset a",
                  "allOf": [
                    {"attribute": "hairType", "equals": "schwarz"},
                    {"attribute": "eyeColor", "equals": "blau"}
                  ]
                }
              ]
            }
            """.trimIndent()

        val ruleSetMap = JsonRuleSetDSL.fromJson<Person>(
            json,
            ruleFactories = mapOf(
                "hairType" to { value -> HairTypeRule(value) },
                "eyeColor" to { value -> EyeColorRule(value) }
            )
        )

        val ruleSetA = ruleSetMap.getValue("Ruleset a")

        assertThat(
            ruleSetA.evaluate(
                Person(
                    age = 30,
                    country = Country.DE,
                    lastName = "Schmidt",
                    hairType = "schwarz",
                    eyeColor = "blau"
                )
            )
        ).isTrue()

        assertThat(
            ruleSetA.evaluate(
                Person(
                    age = 30,
                    country = Country.DE,
                    lastName = "Schmidt",
                    hairType = "blond",
                    eyeColor = "blau"
                )
            )
        ).isFalse()
    }
}
