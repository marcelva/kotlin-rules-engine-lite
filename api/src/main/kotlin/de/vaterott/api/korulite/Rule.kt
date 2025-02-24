package de.vaterott.api.korulite

import de.vaterott.api.domain.Person

interface Rule {
    fun evaluate(person: Person): Boolean
}