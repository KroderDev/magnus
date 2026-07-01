package dev.kroder.magnus.infrastructure.fabric

@file:Suppress("TooGenericExceptionCaught", "SwallowedException")

import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.inventory.Inventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import java.util.Optional

object NbtCompat {

    private const val DEFAULT_NBT_LIST_TYPE = 10

    fun getExhaustion(manager: Any): Float {
        return try {
            val field = manager.javaClass.getDeclaredField("exhaustion")
            field.isAccessible = true
            field.getFloat(manager)
        } catch (e: Exception) {
            try {
                val method = manager.javaClass.getMethod("getExhaustion")
                method.invoke(manager) as Float
            } catch (e2: Exception) {
                0f
            }
        }
    }

    fun setExhaustion(manager: Any, value: Float) {
        try {
            val field = manager.javaClass.getDeclaredField("exhaustion")
            field.isAccessible = true
            field.setFloat(manager, value)
        } catch (e: Exception) {
            try {
                val method = manager.javaClass.getMethod("setExhaustion", Float::class.javaPrimitiveType)
                method.invoke(manager, value)
            } catch (_: Exception) {
            }
        }
    }

    fun getList(compound: NbtCompound, key: String): NbtList {
        return try {
            val method = NbtCompound::class.java.getMethod("getList", String::class.java)
            val opt = method.invoke(compound, key)
            (opt as Optional<NbtList>).orElse(NbtList())
        } catch (e: NoSuchMethodException) {
            val method = NbtCompound::class.java.getMethod(
                "getList",
                String::class.java,
                Int::class.javaPrimitiveType
            )
            method.invoke(compound, key, DEFAULT_NBT_LIST_TYPE) as NbtList
        }
    }

    fun listSize(list: NbtList): Int {
        return try {
            val method = NbtList::class.java.getMethod("size")
            method.invoke(list) as Int
        } catch (e: Exception) {
            (list as Collection<*>).size
        }
    }

    fun getCompound(list: NbtList, index: Int): NbtCompound {
        return try {
            val method = NbtList::class.java.getMethod("getCompound", Int::class.javaPrimitiveType)
            method.invoke(list, index) as NbtCompound
        } catch (e: NoSuchMethodException) {
            val method = NbtList::class.java.getMethod("get", Int::class.javaPrimitiveType)
            method.invoke(list, index) as NbtCompound
        }
    }

    fun writeInventoryNbt(inventory: Inventory, compound: NbtCompound) {
        try {
            val method = Inventory::class.java.getMethod("writeNbt", NbtCompound::class.java)
            method.invoke(inventory, compound)
        } catch (e: NoSuchMethodException) {
            val list = NbtList()
            val type = inventory.javaClass
            val method = type.getMethod("writeNbt", NbtList::class.java)
            val result = method.invoke(inventory, list)
            compound.put("Inventory", result as NbtElement)
        }
    }

    fun readInventoryNbt(inventory: Inventory, compound: NbtCompound) {
        try {
            val method = Inventory::class.java.getMethod("readNbt", NbtCompound::class.java)
            method.invoke(inventory, compound)
        } catch (e: NoSuchMethodException) {
            val list = getList(compound, "Inventory")
            val type = inventory.javaClass
            val method = type.getMethod("readNbt", NbtList::class.java)
            method.invoke(inventory, list)
        }
    }

    fun writeEnderChestNbt(
        enderChest: EnderChestInventory,
        compound: NbtCompound,
        registryManager: RegistryWrapper.WrapperLookup
    ) {
        try {
            val method = EnderChestInventory::class.java.getMethod(
                "writeNbt",
                NbtCompound::class.java,
                RegistryWrapper.WrapperLookup::class.java
            )
            method.invoke(enderChest, compound, registryManager)
        } catch (e: NoSuchMethodException) {
            try {
                val method = EnderChestInventory::class.java.getMethod(
                    "writeNbt",
                    NbtCompound::class.java
                )
                method.invoke(enderChest, compound)
            } catch (e2: NoSuchMethodException) {
                val method = EnderChestInventory::class.java.getMethod(
                    "toNbtList",
                    RegistryWrapper.WrapperLookup::class.java
                )
                val list = method.invoke(enderChest, registryManager) as NbtList
                compound.put("EnderItems", list)
            }
        }
    }

    fun readEnderChestNbt(
        enderChest: EnderChestInventory,
        compound: NbtCompound,
        registryManager: RegistryWrapper.WrapperLookup
    ) {
        try {
            val method = EnderChestInventory::class.java.getMethod(
                "readNbt",
                NbtCompound::class.java
            )
            method.invoke(enderChest, compound)
        } catch (e: NoSuchMethodException) {
            val list = getList(compound, "EnderItems")
            val method = EnderChestInventory::class.java.getMethod(
                "readNbtList",
                NbtList::class.java,
                RegistryWrapper.WrapperLookup::class.java
            )
            method.invoke(enderChest, list, registryManager)
        }
    }

    fun writeStatusEffectNbt(
        effect: StatusEffectInstance,
        registryManager: RegistryWrapper.WrapperLookup
    ): NbtCompound {
        val compound = NbtCompound()
        try {
            val method = StatusEffectInstance::class.java.getMethod(
                "writeNbt",
                RegistryWrapper.WrapperLookup::class.java
            )
            val result = method.invoke(effect, registryManager)
            return result as NbtCompound
        } catch (e: NoSuchMethodException) {
            val method = StatusEffectInstance::class.java.getMethod("writeNbt")
            val result = method.invoke(effect)
            return result as NbtCompound
        }
    }

    fun fromNbtStatusEffect(
        nbt: NbtCompound,
        registryManager: RegistryWrapper.WrapperLookup
    ): StatusEffectInstance? {
        return try {
            val method = StatusEffectInstance::class.java.getMethod(
                "fromNbt",
                RegistryWrapper.WrapperLookup::class.java,
                NbtCompound::class.java
            )
            val result = method.invoke(null, registryManager, nbt)
            (result as? Optional<*>)?.orElse(null) as? StatusEffectInstance
        } catch (e: NoSuchMethodException) {
            val method = StatusEffectInstance::class.java.getMethod(
                "fromNbt",
                NbtCompound::class.java
            )
            method.invoke(null, nbt) as? StatusEffectInstance
        }
    }

    fun writeInventoryCompound(player: ServerPlayerEntity): NbtCompound {
        val compound = NbtCompound()
        writeInventoryNbt(player.inventory, compound)
        return compound
    }

    fun writeEnderChestCompound(player: ServerPlayerEntity): NbtCompound {
        val compound = NbtCompound()
        writeEnderChestNbt(player.enderChestInventory, compound, player.registryManager)
        return compound
    }

    fun writeEffectsCompound(player: ServerPlayerEntity): NbtCompound {
        val compound = NbtCompound()
        val effectsList = NbtList()
        player.statusEffects.forEach { effect ->
            val effectTag = writeStatusEffectNbt(effect, player.registryManager)
            effectsList.add(effectTag)
        }
        compound.put("Effects", effectsList)
        return compound
    }

    fun applyEffectsCompound(player: ServerPlayerEntity, compound: NbtCompound) {
        player.clearStatusEffects()
        val effectsList = getList(compound, "Effects")
        for (i in 0 until listSize(effectsList)) {
            val effectTag = getCompound(effectsList, i)
            val effect = fromNbtStatusEffect(effectTag, player.registryManager)
            if (effect != null) {
                player.addStatusEffect(effect)
            }
        }
    }
}
