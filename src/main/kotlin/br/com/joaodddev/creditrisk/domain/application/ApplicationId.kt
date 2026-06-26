package br.com.joaodddev.creditrisk.domain.application

import java.util.UUID

data class ApplicationId(val value: UUID) {

    companion object {
        fun generate() = ApplicationId(UUID.randomUUID())
        fun of(value: String) = ApplicationId(UUID.fromString(value))
    }

    override fun toString() = value.toString()
}