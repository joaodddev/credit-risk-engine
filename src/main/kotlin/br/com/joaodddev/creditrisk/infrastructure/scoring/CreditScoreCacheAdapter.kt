package br.com.joaodddev.creditrisk.infrastructure.scoring

import br.com.joaodddev.creditrisk.application.port.out.CreditScoreProvider
import br.com.joaodddev.creditrisk.domain.application.CreditScore
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import kotlin.random.Random

@Component
class CreditScoreCacheAdapter(
    private val redisTemplate: StringRedisTemplate
) : CreditScoreProvider {

    private val log = LoggerFactory.getLogger(javaClass)
    private val keyPrefix = "credit:score:"
    private val ttl = Duration.ofHours(1)

    override fun getScore(applicantDocument: String): CreditScore {
        val key = "$keyPrefix$applicantDocument"

        val cached = redisTemplate.opsForValue().get(key)
        if (cached != null) {
            log.info("Cache hit for document={}", applicantDocument)
            return CreditScore.of(cached.toInt())
        }

        val score = simulateExternalScore(applicantDocument)
        redisTemplate.opsForValue().set(key, score.value.toString(), ttl)
        log.info("Score calculated and cached for document={} score={}", applicantDocument, score.value)

        return score
    }

    private fun simulateExternalScore(document: String): CreditScore {
        // Simula uma chamada à bureau de crédito (Serasa/SPC)
        // Em produção: substituir por chamada HTTP ao bureau real
        val seed = document.filter { it.isDigit() }.takeLast(4).toLongOrNull() ?: 500L
        val value = (seed * 7 % 1000).toInt().coerceIn(0, 1000)
        return CreditScore.of(value)
    }
}