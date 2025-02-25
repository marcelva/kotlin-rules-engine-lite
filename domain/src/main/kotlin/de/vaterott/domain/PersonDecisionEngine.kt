@file:Suppress("CdiInjectionPointsInspection")

package de.vaterott.domain

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.DecisionEngine
import de.vaterott.api.korulite.DecisionRuleProvider
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject

@ApplicationScoped
class PersonDecisionEngine @Inject constructor(
    private val ruleProviders: Instance<DecisionRuleProvider<Person>>
) : DecisionEngine<Person> {
    override fun evaluate(decision: String, input: Person): Boolean {
        val provider = ruleProviders.firstOrNull { it.supports(input.country.name) }
            ?: throw IllegalArgumentException("No rule provider found for filter: ${input.country}")
        return provider.getRuleSet(decision).evaluate(input)
    }
}