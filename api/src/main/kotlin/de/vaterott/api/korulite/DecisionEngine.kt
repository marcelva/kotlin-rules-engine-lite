package de.vaterott.api.korulite

interface DecisionEngine<T> {
    fun evaluate(decision: String, input: T): Boolean
}