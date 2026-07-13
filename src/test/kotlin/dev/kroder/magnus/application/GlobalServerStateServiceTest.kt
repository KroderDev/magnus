@file:Suppress("ImportOrdering")

package dev.kroder.magnus.application

import dev.kroder.magnus.domain.messaging.MessageBus
import dev.kroder.magnus.domain.model.ServerStateInfo
import dev.kroder.magnus.domain.model.WorldStateInfo
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GlobalServerStateServiceTest {
    private lateinit var messageBus: MessageBus
    private lateinit var service: GlobalServerStateService
    private val json = Json { ignoreUnknownKeys = true }

    @BeforeEach
    fun setup() {
        messageBus = mockk(relaxed = true)
        service = GlobalServerStateService(messageBus, "test-server")
    }

    @Test
    fun `should update server state map on heartbeat received`() {
        val info = serverState("survival", phase = "night")

        service.onStateReceived(json.encodeToString(info))

        val states = service.getServerStates()
        assertTrue(states.containsKey("survival"))
        assertEquals("night", states["survival"]?.worlds?.first()?.phase)
    }

    @Test
    fun `should replace existing server state on new heartbeat`() {
        service.onStateReceived(json.encodeToString(serverState("survival", phase = "day")))
        service.onStateReceived(json.encodeToString(serverState("survival", phase = "night")))

        val states = service.getServerStates()
        assertEquals("night", states["survival"]?.worlds?.first()?.phase)
    }

    @Test
    fun `should handle malformed server state JSON gracefully`() {
        assertDoesNotThrow {
            service.onStateReceived("invalid json {{{")
        }

        assertEquals(0, service.getServerStates().size)
    }

    @Test
    fun `should cleanup stale entries after timeout`() {
        val staleInfo = serverState(
            serverName = "stale-server",
            phase = "day",
            timestamp = System.currentTimeMillis() - 15_000
        )
        val freshInfo = serverState("fresh-server", phase = "night")

        service.onStateReceived(json.encodeToString(staleInfo))
        service.onStateReceived(json.encodeToString(freshInfo))

        val states = service.getServerStates()
        assertFalse(states.containsKey("stale-server"))
        assertTrue(states.containsKey("fresh-server"))
    }

    @Test
    fun `should shutdown cleanly`() {
        service.onStateReceived(json.encodeToString(serverState("survival", phase = "day")))

        service.shutdown()

        assertEquals(0, service.getServerStates().size)
    }

    private fun serverState(
        serverName: String,
        phase: String,
        timestamp: Long = System.currentTimeMillis()
    ): ServerStateInfo {
        return ServerStateInfo(
            serverName = serverName,
            playerCount = 2,
            maxPlayers = 20,
            worlds = listOf(
                WorldStateInfo(
                    dimension = "minecraft:overworld",
                    timeOfDay = 13500,
                    dayNumber = 42,
                    phase = phase,
                    isDay = phase == "day",
                    isRaining = false,
                    isThundering = false
                )
            ),
            timestamp = timestamp
        )
    }
}
