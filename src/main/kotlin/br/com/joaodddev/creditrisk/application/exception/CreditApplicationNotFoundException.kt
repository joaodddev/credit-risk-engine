package br.com.joaodddev.creditrisk.application.exception

class CreditApplicationNotFoundException(id: String) :
    RuntimeException("Credit application not found: $id")