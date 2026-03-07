package de.vaterott.api.korulite

fun <T> combineWithOr(builder: OrRuleSetBuilder<T>.() -> Unit): RuleSet<T> {
    val ruleSetBuilder = OrRuleSetBuilder<T>()
    ruleSetBuilder.builder()
    return RuleSet(
        listOf(AnyOfRule(ruleSetBuilder.ruleSets.toList()))
    )
}

class AnyOfRule<T>(val alternatives: List<RuleSet<T>>) : Rule<T> {
    override fun evaluate(fact: T): Boolean {
        return alternatives.any { ruleSet ->
            ruleSet.rules.all { rule -> rule.evaluate(fact) }
        }
    }

    override fun toString(): String {
        val content = alternatives.joinToString(prefix = "[", postfix = "]") { it.rules.joinToString(prefix = "[", postfix = "]") }
        return "AnyOfRule(alternatives=$content)"
    }
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
