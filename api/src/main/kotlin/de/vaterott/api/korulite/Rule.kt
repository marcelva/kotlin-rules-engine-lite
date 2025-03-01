package de.vaterott.api.korulite

interface Rule<T> {
    fun evaluate(fact: T): Boolean
}