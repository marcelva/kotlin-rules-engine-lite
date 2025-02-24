package de.vaterott.api

interface Rule {
    fun evaluate(person: Person): Boolean
}