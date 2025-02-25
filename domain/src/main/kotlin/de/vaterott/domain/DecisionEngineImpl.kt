@file:Suppress("CdiInjectionPointsInspection")

package de.vaterott.domain

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Decision
import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.DecisionRuleProvider
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject

@ApplicationScoped
class DecisionEngineImpl @Inject constructor(
    private val ruleProviders: Instance<DecisionRuleProvider<Country>>
) {
    fun evaluate(decision: Decision, person: Person): Boolean {
        val provider = ruleProviders.firstOrNull { it.supports(person.country) }
            ?: throw IllegalArgumentException("No rule provider found for country: ${person.country}")
        return provider.getRuleSet(decision.name).evaluate(person)
    }
}