package de.vaterott.domain.catalog

import de.vaterott.api.korulite.AnyOfRule
import de.vaterott.api.korulite.Rule
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.RuleSetProvider
import de.vaterott.api.domain.Person
import de.vaterott.domain.rulesets.GermanyRuleSetProvider
import de.vaterott.domain.rulesets.LogicalOpSetProvider
import de.vaterott.domain.rulesets.USARuleSetProvider
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

object RuleSetCatalogGenerator {
    internal fun buildMarkdownForTesting(providers: List<RuleSetProvider<Person>>): String = buildMarkdown(providers)

    @JvmStatic
    fun main(args: Array<String>) {
        val outputPath = Path.of(System.getProperty("ruleset.catalog.output", "target/ruleset-catalog.md"))
        val providers = listOf<RuleSetProvider<Person>>(
            GermanyRuleSetProvider(),
            USARuleSetProvider(),
            LogicalOpSetProvider()
        )

        val markdown = buildMarkdown(providers)
        outputPath.parent?.createDirectories()
        outputPath.writeText(markdown)

        println("RuleSet catalog generated: ${outputPath.toAbsolutePath()}")
    }

    private fun buildMarkdown(providers: List<RuleSetProvider<Person>>): String {
        val body = providers.joinToString("\n\n") { provider ->
            val marketLabel = provider.marketingLabel()
            val rows = provider.listRuleSets()
                .toSortedMap()
                .entries
                .joinToString("\n") { (action, ruleSet) ->
                    "| `${action}` | ${toHumanRuleDescription(ruleSet)} |"
                }

            """
            ## $marketLabel

            | Business-Entscheidung | Gültig, wenn... |
            |---|---|
            $rows
            """.trimIndent()
        }

        return """
        # Business RuleSet Katalog

        Diese Übersicht zeigt die aktuell hinterlegten Regeln in einer marketingtauglichen, nicht-technischen Form.
        
        > Generiert automatisch per Maven-Target.

        $body
        """.trimIndent() + "\n"
    }

    private fun RuleSetProvider<Person>.marketingLabel(): String = when {
        supports("DE") -> "🇩🇪 Deutschland"
        supports("USA") -> "🇺🇸 USA"
        supports("LOGICALOP") -> "🧠 Logische Kombinationsregeln"
        else -> this::class.simpleName ?: "Unbekannt"
    }

    private fun toHumanRuleDescription(ruleSet: RuleSet<Person>): String = ruleSet.rules.joinToString(" und ") { rule ->
        when (rule) {
            is AnyOfRule<Person> -> rule.alternatives.joinToString(" oder ", prefix = "(", postfix = ")") { alt ->
                alt.rules.joinToString(" und ") { nested -> formatSimpleRule(nested) }
            }
            else -> formatSimpleRule(rule)
        }
    }

    private fun formatSimpleRule(rule: Rule<Person>): String {
        val printable = rule.toString()
        return printable
            .replace("MinAgeRule(minAge=", "Mindestalter ")
            .replace(")", "")
            .replace("LastNamePrefixRule(prefix='", "Nachname beginnt mit '")
            .replace("SameAgeRule(age=", "Alter ist genau ")
            .replace("IsSameDayRule(dow=", "Tag ist ")
    }
}
