package de.vaterott.api.korulite

interface RuleSetSupplier<T> {
    fun evaluate(action: String, fact: T): Boolean
}