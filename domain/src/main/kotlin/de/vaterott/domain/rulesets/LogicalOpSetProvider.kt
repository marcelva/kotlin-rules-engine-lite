package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import de.vaterott.api.domain.PersonAction
import de.vaterott.api.korulite.ProviderDoc
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.RuleSetDoc
import de.vaterott.api.korulite.RuleSetProvider
import de.vaterott.api.korulite.combineWithOr
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.LastNamePrefixRule
import de.vaterott.domain.rules.MinAgeRule
import de.vaterott.domain.rules.SameAgeRule
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@ProviderDoc("🧠 Logische Kombinationsregeln")
class LogicalOpSetProvider : RuleSetProvider<Person> {

    @RuleSetDoc(
        action = "CAN_DRINK_BEER",
        title = "Alternative Freigabe",
        summary = "Erfüllt, wenn einer von mehreren Regelpfaden greift"
    )
    private val canDrinkBeer = combineWithOr<Person> {
        add(ruleSet {
            rule(MinAgeRule(18))
            rule(LastNamePrefixRule("O"))
        })
        add(ruleSet {
            rule(SameAgeRule(12))
        })
    }

    @RuleSetDoc(
        action = "CAN_DRIVE_RIGHT",
        title = "Alternative Fahrtfreigabe",
        summary = "Verwendet dieselbe OR-Logik wie die alternative Freigabe"
    )
    private val canDriveRight = canDrinkBeer

    private val knowledgeBase: Map<String, RuleSet<Person>> = mapOf(
        PersonAction.CAN_DRINK_BEER.name to canDrinkBeer,
        PersonAction.CAN_DRIVE_RIGHT.name to canDriveRight
    )

    override fun supports(filterBy: String) = filterBy == Country.LOGICALOP.name

    override fun listRuleSets(): Map<String, RuleSet<Person>> = knowledgeBase

    override fun getRuleSet(action: String): RuleSet<Person> {
        return knowledgeBase[action]
            ?: throw IllegalArgumentException("Action $action not supported for ${this::class.java.simpleName}.")
    }
}
