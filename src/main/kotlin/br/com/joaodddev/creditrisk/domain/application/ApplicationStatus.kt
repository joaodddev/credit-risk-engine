package br.com.joaodddev.creditrisk.domain.application

enum class ApplicationStatus {
    PENDING,
    UNDER_REVIEW,
    APPROVED,
    REJECTED;

    fun transitionTo(next: ApplicationStatus): ApplicationStatus {
        val allowed = allowedTransitions[this] ?: emptySet()
        require(next in allowed) {
            "Invalid transition: $this → $next"
        }
        return next
    }

    companion object {
        private val allowedTransitions = mapOf(
            PENDING      to setOf(UNDER_REVIEW),
            UNDER_REVIEW to setOf(APPROVED, REJECTED)
        )
    }
}