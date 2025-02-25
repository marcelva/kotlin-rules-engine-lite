package de.vaterott.api.korulite

interface Rule<T> {
    fun evaluate(input: T): Boolean
}