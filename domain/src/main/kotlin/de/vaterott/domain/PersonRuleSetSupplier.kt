@file:Suppress("CdiInjectionPointsInspection")

package de.vaterott.domain

import de.vaterott.api.domain.Person
import de.vaterott.api.korulite.RuleSetSupplier
import de.vaterott.api.korulite.RuleSetProvider
import io.quarkus.cache.CacheResult
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.inject.Inject

@ApplicationScoped
class PersonRuleSetSupplier @Inject constructor(
    private val ruleProviders: Instance<RuleSetProvider<Person>>
) : RuleSetSupplier<Person> {

    @CacheResult(cacheName = "person-rule-sets")
    override fun evaluate(action: String, fact: Person): Boolean {
        val provider = ruleProviders.firstOrNull { it.supports(fact.country.name) }
            ?: throw IllegalArgumentException("No rule set provider found for filter: ${fact.country}")
        return provider.getRuleSet(action).evaluate(fact)
    }
}