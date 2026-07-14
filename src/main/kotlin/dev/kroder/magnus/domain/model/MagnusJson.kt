package dev.kroder.magnus.domain.model

import kotlinx.serialization.json.Json

/**
 * Shared, thread-safe [Json] configuration used across all Magnus repositories,
 * modules, and application services.
 *
 * Centralizing the [Json] instance avoids duplicate reflection metadata, descriptor
 * initialization, and object allocations on every service or repository instantiation.
 */
val MagnusJson = Json {
    ignoreUnknownKeys = true
}

/**
 * Shared, thread-safe [Json] configuration with pretty printing enabled,
 * used for human-readable configuration files and local emergency backups.
 */
val MagnusPrettyJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}
