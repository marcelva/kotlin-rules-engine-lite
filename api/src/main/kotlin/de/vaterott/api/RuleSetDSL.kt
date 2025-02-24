package de.vaterott.api

fun ruleSet(builder: RuleSetBuilder.() -> Unit): RuleSet {
    val ruleSetBuilder = RuleSetBuilder()
    ruleSetBuilder.builder()
    return RuleSet(ruleSetBuilder.rules)
}

class RuleSetBuilder {
    val rules = mutableListOf<Rule>()

    fun rule(rule: Rule) {
        rules.add(rule)
    }
}