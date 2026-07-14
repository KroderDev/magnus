@file:Suppress("TooGenericExceptionCaught", "SwallowedException")

package dev.kroder.magnus.application

import dev.kroder.magnus.domain.messaging.MessageBus
import dev.kroder.magnus.domain.model.MagnusJson
import dev.kroder.magnus.domain.model.ServerStateInfo
import dev.kroder.magnus.domain.model.WorldStateInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

private const val MINECRAFT_DAY_LENGTH = 24_000L
private const val SUNRISE_END = 1_000L
private const val DAY_END = 12_000L
private const val SUNSET_END = 13_000L
private const val NIGHT_END = 23_000L

/**
 * Application service for read-only server state synchronization.
 * Publishes live server/world facts for external agent tooling.
 */
class GlobalServerStateService(
    private val messageBus: MessageBus,
    private val serverName: String,
    private val heartbeatIntervalMs: Long = HEARTBEAT_INTERVAL_MS
) {
    private val logger = LoggerFactory.getLogger("magnus-global-server-state")
    private val json = MagnusJson

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var heartbeatJob: Job? = null

    private val serverStates = ConcurrentHashMap<String, ServerStateInfo>()
    private val lastCleanup = AtomicLong(0L)

    companion object {
        const val CHANNEL = "magnus:serverstate"
        const val HEARTBEAT_INTERVAL_MS = 2500L
        private const val STALE_TIMEOUT_MS = 10_000L
        private const val CLEANUP_INTERVAL_MS = 1000L
    }

    fun startHeartbeat(server: MinecraftServer) {
        heartbeatJob = scope.launch {
            while (isActive) {
                try {
                    publishHeartbeat(server)
                } catch (e: Exception) {
                    logger.error("Server state heartbeat failed: ${e.message}", e)
                }
                delay(heartbeatIntervalMs)
            }
        }
        logger.info("Global server state heartbeat started (interval: ${heartbeatIntervalMs}ms)")
    }

    fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
        logger.info("Global server state heartbeat stopped")
    }

    private fun publishHeartbeat(server: MinecraftServer) {
        val info = buildServerState(server)

        serverStates[serverName] = info
        messageBus.publish(CHANNEL, json.encodeToString(info))
    }

    fun onStateReceived(payload: String) {
        try {
            val info = json.decodeFromString<ServerStateInfo>(payload)
            serverStates[info.serverName] = info
            cleanupStaleEntries()
        } catch (e: Exception) {
            logger.error("Failed to process server state heartbeat: ${e.message}", e)
        }
    }

    fun getServerStates(): Map<String, ServerStateInfo> {
        cleanupStaleEntries()
        return serverStates.toMap()
    }

    fun shutdown() {
        stopHeartbeat()
        scope.cancel()
        serverStates.clear()
    }

    private fun buildServerState(server: MinecraftServer): ServerStateInfo {
        return ServerStateInfo(
            serverName = serverName,
            playerCount = server.playerManager.playerList.size,
            maxPlayers = server.playerManager.maxPlayerCount,
            worlds = server.worlds.map { world ->
                val rawTimeOfDay = world.timeOfDay
                val timeOfDay = Math.floorMod(rawTimeOfDay, MINECRAFT_DAY_LENGTH)
                WorldStateInfo(
                    dimension = world.registryKey.value.toString(),
                    timeOfDay = timeOfDay,
                    dayNumber = rawTimeOfDay / MINECRAFT_DAY_LENGTH,
                    phase = phaseForTime(timeOfDay),
                    isDay = timeOfDay < DAY_END,
                    isRaining = world.isRaining,
                    isThundering = world.isThundering
                )
            }.toList()
        )
    }

    private fun phaseForTime(timeOfDay: Long): String {
        return when {
            timeOfDay < SUNRISE_END -> "sunrise"
            timeOfDay < DAY_END -> "day"
            timeOfDay < SUNSET_END -> "sunset"
            timeOfDay < NIGHT_END -> "night"
            else -> "sunrise"
        }
    }

    private fun cleanupStaleEntries() {
        val now = System.currentTimeMillis()
        if (now - lastCleanup.get() < CLEANUP_INTERVAL_MS) return
        lastCleanup.set(now)
        serverStates.entries.removeIf { (name, info) ->
            name != serverName && (now - info.timestamp) > STALE_TIMEOUT_MS
        }
    }
}
