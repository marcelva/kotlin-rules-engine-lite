package de.vaterott.api.korulite

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