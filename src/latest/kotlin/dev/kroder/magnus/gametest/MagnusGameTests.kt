package dev.kroder.magnus.gametest

import dev.kroder.magnus.domain.model.PlayerData
import dev.kroder.magnus.infrastructure.fabric.FabricPlayerAdapter
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.network.packet.c2s.common.SyncedClientOptions
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.test.TestContext
import java.util.UUID
import com.mojang.authlib.GameProfile
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class MagnusGameTests {

    @GameTest
    fun adapterRoundtrip(context: TestContext) {
        val server = context.world.server
        val world = context.world
        val profile = GameProfile(UUID.randomUUID(), "TestPlayer")

        val clientOptions = SyncedClientOptions.createDefault()
        val player = ServerPlayerEntity(server, world, profile, clientOptions)

        player.health = 10f
        player.hungerManager.foodLevel = 15
        player.hungerManager.saturationLevel = 2f
        player.inventory.setStack(0, ItemStack(Items.DIAMOND, 64))
        player.setExperienceLevel(5)
        player.experienceProgress = 0.5f

        val snapshot: PlayerData = FabricPlayerAdapter.toDomain(player)

        if (snapshot.health != 10f) throw RuntimeException(
            "Snapshot Health mismatch: expected 10.0, got ${snapshot.health}"
        )
        if (snapshot.foodLevel != 15) throw RuntimeException(
            "Snapshot Food mismatch: expected 15, got ${snapshot.foodLevel}"
        )
        if (snapshot.experienceLevel != 5) throw RuntimeException(
            "Snapshot XP Level mismatch: expected 5, got ${snapshot.experienceLevel}"
        )

        player.health = 20f
        player.inventory.clear()
        player.setExperienceLevel(0)

        FabricPlayerAdapter.applyToPlayer(player, snapshot)

        if (player.health != 10f) throw RuntimeException(
            "Restored Health mismatch: expected 10.0, got ${player.health}"
        )
        if (player.hungerManager.foodLevel != 15) throw RuntimeException(
            "Restored Food mismatch: expected 15, got ${player.hungerManager.foodLevel}"
        )

        val stack = player.inventory.getStack(0)
        if (!stack.isOf(Items.DIAMOND) || stack.count != 64) {
             throw RuntimeException(
                 "Restored Inventory mismatch: expected 64 Diamond, got ${stack.count} ${stack.item}"
             )
        }

        context.complete()
    }
}
