@file:Suppress("TooGenericExceptionCaught", "SwallowedException")

package dev.kroder.magnus.infrastructure.persistence.local

import dev.kroder.magnus.domain.model.MagnusPrettyJson
import dev.kroder.magnus.domain.model.PlayerData
import dev.kroder.magnus.domain.port.PlayerRepository
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.UUID

/**
 * Repository implementation that saves data to the local file system.
 * Acts as a "Last Line of Defense" (Backup) when databases fail.
 *
 * Functional Limits:
 * - Stores files in config/magnus/backups/<uuid>.json
 * - Not performant for normal operations, strictly for emergency/recovery.
 */
class LocalBackupRepository(
    private val rootDir: File
) : PlayerRepository {

    private val json = MagnusPrettyJson

    private val logger = LoggerFactory.getLogger("magnus-local-backup")

    // O(1) In-Memory Index (Dirty Set)
    private val pendingBackups = java.util.concurrent.ConcurrentHashMap.newKeySet<UUID>()

    init {
        if (!rootDir.exists()) {
            rootDir.mkdirs()
        }
        // Async scan on startup to populate the index
        java.util.concurrent.CompletableFuture.runAsync {
            val files = rootDir.listFiles { _, name -> name.endsWith(".json") }
            files?.forEach { file ->
                try {
                    val uuidStr = file.nameWithoutExtension
                    val uuid = UUID.fromString(uuidStr)
                    pendingBackups.add(uuid)
                } catch (e: Exception) {
                    logger.warn("Failed to parse backup filename", e)
                }
            }
        }
    }

    override fun save(data: PlayerData) {
        try {
            val file = File(rootDir, "${data.uuid}.json")
            val content = json.encodeToString(PlayerData.serializer(), data)
            Files.writeString(file.toPath(), content)

            // Mark as dirty in memory
            pendingBackups.add(data.uuid)
        } catch (e: IOException) {
            logger.warn("Failed to save backup file", e)
        }
    }

    override fun findByUuid(uuid: UUID): PlayerData? {
        // Optimization: If not in dirty set, don't touch disk
        var result: PlayerData? = null

        if (pendingBackups.contains(uuid)) {
            val file = File(rootDir, "$uuid.json")
            if (file.exists()) {
                try {
                    val content = Files.readString(file.toPath())
                    result = json.decodeFromString(PlayerData.serializer(), content)
                } catch (e: Exception) {
                    logger.warn("Failed to read backup file", e)
                }
            } else {
                pendingBackups.remove(uuid)
            }
        }

        return result
    }

    override fun deleteCache(uuid: UUID) {
        // We do NOT delete local backups on "cache release".
    }

    /**
     * Checks if a backup exists without touching the disk (O(1)).
     */
    fun hasBackup(uuid: UUID): Boolean {
        return pendingBackups.contains(uuid)
    }

    fun findAllStartups(): List<PlayerData> {
        // Use the index to find files faster or just scan directory (safer for janitor)
        // For Janitor, we stick to directory scan to be source of truth
        val files = rootDir.listFiles { _, name -> name.endsWith(".json") } ?: return emptyList()
        return files.mapNotNull { file ->
            try {
                val content = Files.readString(file.toPath())
                json.decodeFromString(PlayerData.serializer(), content)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun deleteFile(uuid: UUID) {
        val file = File(rootDir, "$uuid.json")
        if (file.exists()) {
            file.delete()
        }
        // Remove from index
        pendingBackups.remove(uuid)
    }
}
