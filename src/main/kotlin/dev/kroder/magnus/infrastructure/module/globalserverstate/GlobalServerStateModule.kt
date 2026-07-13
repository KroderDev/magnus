package dev.kroder.magnus.infrastructure.module.globalserverstate

import dev.kroder.magnus.application.GlobalServerStateService
import dev.kroder.magnus.domain.messaging.MessageBus
import dev.kroder.magnus.domain.module.MagnusModule
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer
import org.slf4j.LoggerFactory

/**
 * Module for read-only live server state synchronization.
 */
class GlobalServerStateModule(
    private val messageBus: MessageBus,
    private val serverName: String,
    private val heartbeatIntervalMs: Long
) : MagnusModule {

    override val id = "global-server-state"
    override val name = "Global Server State"

    private val logger = LoggerFactory.getLogger("magnus-global-server-state")

    private lateinit var serverStateService: GlobalServerStateService
    private var server: MinecraftServer? = null

    override fun onEnable() {
        serverStateService = GlobalServerStateService(messageBus, serverName, heartbeatIntervalMs)

        messageBus.subscribe(GlobalServerStateService.CHANNEL) { payload ->
            serverStateService.onStateReceived(payload)
        }

        ServerLifecycleEvents.SERVER_STARTED.register { srv ->
            server = srv
            serverStateService.startHeartbeat(srv)
            logger.info("Global Server State module activated for server: $serverName")
        }

        ServerLifecycleEvents.SERVER_STOPPING.register {
            serverStateService.stopHeartbeat()
        }

        logger.info("Global Server State module enabled")
    }

    override fun onDisable() {
        serverStateService.shutdown()
        server = null
        logger.info("Global Server State module disabled")
    }
}
