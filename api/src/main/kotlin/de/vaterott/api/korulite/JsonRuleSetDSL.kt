package de.vaterott.api.korulite

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonRulesDocument(
    val ruleSets: List<JsonRuleSetDefinition> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonRuleSetDefinition(
    val name: String,
    val allOf: List<JsonConditionDefinition> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonConditionDefinition(
    val attribute: String,
    val equals: String
)

object JsonRuleSetDSL {
    private val mapper = jacksonObjectMapper()

    fun <T> fromJson(
        json: String,
        ruleFactories: Map<String, (String) -> Rule<T>>
    ): Map<String, RuleSet<T>> {
        val document = mapper.readValue<JsonRulesDocument>(json)
        return document.ruleSets.associate { definition ->
            definition.name to RuleSet(
                definition.allOf.map { condition ->
                    val factory = ruleFactories[condition.attribute]
                        ?: throw IllegalArgumentException("No rule factory registered for attribute '${condition.attribute}'.")
                    factory(condition.equals)
                }
            )
        }
    }
}
