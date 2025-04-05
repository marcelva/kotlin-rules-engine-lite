package de.vaterott.domain.rulesets

import de.vaterott.api.domain.Country
import de.vaterott.api.domain.Person
import de.vaterott.api.domain.PersonAction
import de.vaterott.api.korulite.RuleSet
import de.vaterott.api.korulite.RuleSetProvider
import de.vaterott.api.korulite.combineWithOr
import de.vaterott.api.korulite.ruleSet
import de.vaterott.domain.rules.LastNamePrefixRule
import de.vaterott.domain.rules.MinAgeRule
import de.vaterott.domain.rules.SameAgeRule
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class LogicalOpSetProvider : RuleSetProvider<Person> {

    private val orRuleSet = combineWithOr<Person> {
        add(ruleSet {
            rule(MinAgeRule(18))
            rule(LastNamePrefixRule("O"))
        })
        add(ruleSet {
            rule(SameAgeRule(12))
        })
    }


    private val knowledgeBase: Map<String, RuleSet<Person>> = mapOf(
        PersonAction.CAN_DRINK_BEER.name to orRuleSet,
        PersonAction.CAN_DRIVE_RIGHT.name to orRuleSet
    )

    override fun supports(filterBy: String) = filterBy == Country.LOGICALOP.name

    override fun getRuleSet(action: String): RuleSet<Person> {
        println("(LogicalOp) Checking for rule set for action: $action in $knowledgeBase")
        return knowledgeBase[action]
            ?: throw IllegalArgumentException("Action $action not supported for ${this::class.java.simpleName}.")
    }


}