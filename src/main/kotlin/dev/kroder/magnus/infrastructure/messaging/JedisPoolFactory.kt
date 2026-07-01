package dev.kroder.magnus.infrastructure.messaging

import org.slf4j.LoggerFactory
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

/**
 * Factory for creating JedisPool instances with optional SSL/TLS support.
 *
 * Security Features:
 * - SSL/TLS encryption support
 * - Custom truststore for certificate verification
 * - Connection pooling with health checks
 */
object JedisPoolFactory {

    private const val DEFAULT_TIMEOUT_MS = 2000
    private const val POOL_MAX_TOTAL = 16
    private const val POOL_MAX_IDLE = 8
    private const val POOL_MIN_IDLE = 2
    private const val POOL_MAX_WAIT_SECONDS = 5L
    private val POOL_MAX_WAIT = java.time.Duration.ofSeconds(POOL_MAX_WAIT_SECONDS)

    private val logger = LoggerFactory.getLogger("magnus-jedis-factory")

    /**
     * Creates a JedisPool with the specified configuration.
     *
     * @param host Redis server hostname
     * @param port Redis server port
     * @param password Redis password (optional)
     * @param useSsl Whether to use SSL/TLS encryption
     * @param truststorePath Path to JKS truststore file (optional, uses system default if null)
     * @param truststorePassword Password for the truststore
     * @param timeoutMs Connection timeout in milliseconds
     * @return Configured JedisPool instance
     */
    @Suppress("LongParameterList")
    fun create(
        host: String,
        port: Int,
        password: String? = null,
        useSsl: Boolean = false,
        truststorePath: String? = null,
        truststorePassword: String = "changeit",
        timeoutMs: Int = DEFAULT_TIMEOUT_MS
    ): JedisPool {
        val config = JedisPoolConfig().apply {
            maxTotal = POOL_MAX_TOTAL
            maxIdle = POOL_MAX_IDLE
            minIdle = POOL_MIN_IDLE
            testOnBorrow = true
            testWhileIdle = true
            // Block for at most 5 seconds when pool is exhausted
            blockWhenExhausted = true
            setMaxWait(POOL_MAX_WAIT)
        }

        return if (useSsl) {
            logger.info("Creating SSL-enabled JedisPool for $host:$port")

            val sslContext = if (truststorePath != null) {
                logger.info("Using custom truststore: $truststorePath")
                createSslContext(truststorePath, truststorePassword)
            } else {
                SSLContext.getDefault()
            }

            JedisPool(
                config,
                host,
                port,
                timeoutMs,
                password,
                0, // database
                "magnus", // clientName
                useSsl,
                sslContext.socketFactory,
                sslContext.defaultSSLParameters,
                null // hostnameVerifier
            )
        } else {
            logger.info("Creating JedisPool for $host:$port (SSL disabled)")

            if (password.isNullOrEmpty()) {
                JedisPool(config, host, port, timeoutMs)
            } else {
                JedisPool(config, host, port, timeoutMs, password)
            }
        }
    }

    private fun createSslContext(truststorePath: String, password: String): SSLContext {
        val trustStore = KeyStore.getInstance("JKS")
        FileInputStream(truststorePath).use { fis ->
            trustStore.load(fis, password.toCharArray())
        }

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(trustStore)

        return SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers, null)
        }
    }
}
