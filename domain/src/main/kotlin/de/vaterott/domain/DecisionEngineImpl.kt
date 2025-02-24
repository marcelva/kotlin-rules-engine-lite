@file:Suppress("CdiInjectionPointsInspection")

package de.vaterott.domain

import de.vaterott.api.korulite.CountryDecisionRuleProvider
import de.vaterott.api.domain.Decision
import de.vaterott.api.domain.Person
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject

// Todo Until now, the DecisionEngineImpl only deals with CountryDecisionRuleProviders.. should be concretized
@ApplicationScoped
class DecisionEngineImpl @Inject constructor(
    private val ruleProviders: Instance<CountryDecisionRuleProvider>
) {
    fun evaluate(decision: Decision, person: Person): Boolean {
        val provider = ruleProviders.firstOrNull { it.supports(person.country) }
            ?: throw IllegalArgumentException("No rule provider found for country: ${person.country}")
        return provider.getRuleSet(decision).evaluate(person)
    }
}