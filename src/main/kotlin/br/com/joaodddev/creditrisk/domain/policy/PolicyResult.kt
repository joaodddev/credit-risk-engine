package br.com.joaodddev.creditrisk.domain.policy

sealed class PolicyResult {
    object Approved : PolicyResult()
    data class Rejected(val reason: String) : PolicyResult()

    val isApproved get() = this is Approved
}