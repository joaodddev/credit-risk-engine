package br.com.joaodddev.creditrisk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CreditRiskEngineApplication

fun main(args: Array<String>) {
	runApplication<CreditRiskEngineApplication>(*args)
}
