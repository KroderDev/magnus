package dev.kroder.magnus.domain.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ServerStateInfoTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `should serialize with world state correctly`() {
        val original = ServerStateInfo(
            serverName = "survival",
            playerCount = 3,
            maxPlayers = 100,
            worlds = listOf(
                WorldStateInfo(
                    dimension = "minecraft:overworld",
                    timeOfDay = 13500,
                    dayNumber = 42,
                    phase = "night",
                    isDay = false,
                    isRaining = false,
                    isThundering = false
                )
            ),
            timestamp = 1706654400000
        )

        val serialized = json.encodeToString(original)
        val deserialized = json.decodeFromString<ServerStateInfo>(serialized)

        assertEquals(original, deserialized)
        assertEquals("minecraft:overworld", deserialized.worlds.first().dimension)
    }

    @Test
    fun `should set default timestamp when not provided`() {
        val before = System.currentTimeMillis()

        val info = ServerStateInfo(
            serverName = "test",
            playerCount = 0,
            maxPlayers = 20,
            worlds = emptyList()
        )

        val after = System.currentTimeMillis()

        assertTrue(info.timestamp >= before)
        assertTrue(info.timestamp <= after)
    }
}
