package de.vaterott.domain.catalog

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.AnyOfRule
import de.vaterott.api.korulite.ProviderDoc
import de.vaterott.api.korulite.Rule
import de.vaterott.api.korulite.RuleDoc
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.RuleSetDoc
import de.vaterott.api.korulite.RuleSetProvider
import de.vaterott.domain.rulesets.GermanyRuleSetProvider
import de.vaterott.domain.rulesets.LogicalOpSetProvider
import de.vaterott.domain.rulesets.USARuleSetProvider
import java.lang.reflect.Field
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

object RuleSetCatalogGenerator {

    @JvmStatic
    fun main(args: Array<String>) {
        val outputPath = Path.of(System.getProperty("ruleset.catalog.output", "target/ruleset-catalog.md"))
        val providers = listOf<RuleSetProvider<Person>>(
            GermanyRuleSetProvider(),
            USARuleSetProvider(),
            LogicalOpSetProvider()
        )

        val markdown = buildMarkdownForTesting(providers)
        outputPath.parent?.createDirectories()
        outputPath.writeText(markdown)

        println("RuleSet catalog generated: ${outputPath.toAbsolutePath()}")
    }

    internal fun buildMarkdownForTesting(providers: List<RuleSetProvider<Person>>): String {
        val body = providers.joinToString("\n\n") { provider ->
            val providerLabel = provider.providerLabel()
            val ruleSetDocs = provider.ruleSetDocsByAction()
            val rows = provider.listRuleSets()
                .toSortedMap()
                .entries
                .joinToString("\n") { (action, ruleSet) ->
                    val doc = ruleSetDocs[action]
                    val title = doc?.title?.takeIf { it.isNotBlank() } ?: action
                    val summary = doc?.summary?.takeIf { it.isNotBlank() }?.let { "<br/><sub>$it</sub>" } ?: ""
                    "| $title$summary | ${toHumanRuleDescription(ruleSet)} |"
                }

            """
            ## $providerLabel

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

    private fun RuleSetProvider<Person>.providerLabel(): String {
        return this::class.java.getAnnotation(ProviderDoc::class.java)?.label
            ?: (this::class.simpleName ?: "Unbekannt")
    }

    private fun RuleSetProvider<Person>.ruleSetDocsByAction(): Map<String, RuleSetDoc> {
        return this::class.java.declaredFields
            .mapNotNull { field ->
                val doc = field.getAnnotation(RuleSetDoc::class.java) ?: return@mapNotNull null
                doc.action to doc
            }
            .toMap()
    }

    private fun toHumanRuleDescription(ruleSet: RuleSet<Person>): String {
        return ruleSet.rules.joinToString(" und ") { rule ->
            when (rule) {
                is AnyOfRule<Person> -> rule.alternatives.joinToString(" oder ", prefix = "(", postfix = ")") { alt ->
                    alt.rules.joinToString(" und ") { nested -> describeRule(nested) }
                }

                else -> describeRule(rule)
            }
        }
    }

    private fun describeRule(rule: Rule<Person>): String {
        val ruleClass = rule::class.java
        val template = ruleClass.getAnnotation(RuleDoc::class.java)?.template ?: return rule.toString()
        return interpolateTemplate(template, rule)
    }

    private fun interpolateTemplate(template: String, instance: Any): String {
        var resolved = template
        allFields(instance::class.java).forEach { field ->
            field.isAccessible = true
            val key = "{${field.name}}"
            val value = field.get(instance)?.toString() ?: ""
            resolved = resolved.replace(key, value)
        }
        return resolved
    }

    private fun allFields(type: Class<*>): List<Field> {
        val fields = mutableListOf<Field>()
        var current: Class<*>? = type
        while (current != null && current != Any::class.java) {
            fields += current.declaredFields
            current = current.superclass
        }
        return fields
    }
}
