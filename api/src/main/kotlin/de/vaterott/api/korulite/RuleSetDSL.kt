package de.vaterott.api.korulite

fun <T> combineWithOr(builder: OrRuleSetBuilder<T>.() -> Unit): RuleSet<T> {
    val ruleSetBuilder = OrRuleSetBuilder<T>()
    ruleSetBuilder.builder()
    return RuleSet(
        listOf(object : Rule<T> {
            override fun evaluate(fact: T): Boolean {
                return ruleSetBuilder.ruleSets.any { ruleSet ->
                    ruleSet.rules.all { rule -> rule.evaluate(fact) }
                }
            }
        })
    )
}

class OrRuleSetBuilder<T> {
    val ruleSets = mutableListOf<RuleSet<T>>()

    fun add(ruleSet: RuleSet<T>) {
        println("Adding ruleSet: $ruleSet")
        ruleSets.add(ruleSet)
    }
}

fun <T> ruleSet(builder: RuleSetBuilder<T>.() -> Unit): RuleSet<T> {
    val ruleSetBuilder = RuleSetBuilder<T>()
    ruleSetBuilder.builder()
    return RuleSet(ruleSetBuilder.rules)
}

class RuleSetBuilder<T> {
    val rules = mutableListOf<Rule<T>>()

    fun rule(rule: Rule<T>) {
        rules.add(rule)
    }
}