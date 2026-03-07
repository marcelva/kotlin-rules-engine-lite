package de.vaterott.domain.catalog

import de.vaterott.domain.rulesets.GermanyRuleSetProvider
import de.vaterott.domain.rulesets.LogicalOpSetProvider
import de.vaterott.domain.rulesets.USARuleSetProvider
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

class RuleSetCatalogGeneratorTest {

    @Test
    fun `should generate business friendly markdown`() {
        val markdown = RuleSetCatalogGenerator.buildMarkdownForTesting(
            listOf(
                GermanyRuleSetProvider(),
                USARuleSetProvider(),
                LogicalOpSetProvider()
            )
        )

        assertContains(markdown, "# Business RuleSet Katalog")
        assertContains(markdown, "🇩🇪 Deutschland")
        assertContains(markdown, "CAN_DRINK_BEER")
        assertContains(markdown, "Mindestalter")
        assertContains(markdown, "oder")
    }
}
