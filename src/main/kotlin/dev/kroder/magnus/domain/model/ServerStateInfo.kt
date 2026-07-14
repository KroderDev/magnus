package dev.kroder.magnus.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents live read-only server facts for heartbeat synchronization.
 */
@Serializable
data class ServerStateInfo(
    val serverName: String,
    val playerCount: Int,
    val maxPlayers: Int,
    val worlds: List<WorldStateInfo>,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Represents read-only world facts that are safe to expose to agents.
 */
@Serializable
data class WorldStateInfo(
    val dimension: String,
    val timeOfDay: Long,
    val dayNumber: Long,
    val phase: String,
    val isDay: Boolean,
    val isRaining: Boolean,
    val isThundering: Boolean
)
