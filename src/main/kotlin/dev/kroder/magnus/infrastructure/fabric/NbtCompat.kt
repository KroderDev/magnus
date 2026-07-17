@file:Suppress("TooGenericExceptionCaught", "SwallowedException", "TooManyFunctions")

package dev.kroder.magnus.infrastructure.fabric

import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.inventory.EnderChestInventory
import net.minecraft.inventory.Inventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.Optional

object NbtCompat {

    private val EXHAUSTION_FIELD_NAMES = setOf("exhaustion", "field_7752", "c", "e")
    private val GET_EXHAUSTION_METHOD_NAMES = setOf("getExhaustion", "method_35219", "d")
    private val SET_EXHAUSTION_METHOD_NAMES = setOf("setExhaustion", "method_35218", "c")

    fun getExhaustion(manager: Any): Float {
        return try {
            val field = findField(manager.javaClass, EXHAUSTION_FIELD_NAMES)
                ?: throw NoSuchFieldException("exhaustion")
            field.getFloat(manager)
        } catch (_: Exception) {
            try {
                val method = findMethod(
                    manager.javaClass,
                    GET_EXHAUSTION_METHOD_NAMES,
                    emptyArray(),
                    Float::class.javaPrimitiveType
                ) ?: throw NoSuchMethodException("getExhaustion")
                method.invoke(manager) as Float
            } catch (_: Exception) {
                0f
            }
        }
    }

    fun setExhaustion(manager: Any, value: Float) {
        try {
            val field = findField(manager.javaClass, EXHAUSTION_FIELD_NAMES)
                ?: throw NoSuchFieldException("exhaustion")
            field.setFloat(manager, value)
        } catch (_: Exception) {
            try {
                val method = findMethod(
                    manager.javaClass,
                    SET_EXHAUSTION_METHOD_NAMES,
                    arrayOf(Float::class.javaPrimitiveType!!),
                    Void.TYPE
                ) ?: throw NoSuchMethodException("setExhaustion")
                method.invoke(manager, value)
            } catch (_: Exception) {
            }
        }
    }

    fun getList(compound: NbtCompound, key: String): NbtList {
        return (compound.get(key) as? NbtList) ?: NbtList()
    }

    fun listSize(list: NbtList): Int {
        return list.size
    }

    fun getCompound(list: NbtList, index: Int): NbtCompound {
        return list[index] as NbtCompound
    }

    fun writeInventoryNbt(inventory: Inventory, compound: NbtCompound) {
        val compoundMethod = findMethod(
            inventory.javaClass,
            arrayOf(NbtCompound::class.java)
        )
        if (compoundMethod != null) {
            compoundMethod.invoke(inventory, compound)
            return
        }

        val listMethod = findMethod(
            inventory.javaClass,
            arrayOf(NbtList::class.java),
            NbtList::class.java
        ) ?: throw NoSuchMethodException("inventory write NBT")
        val list = NbtList()
        val result = listMethod.invoke(inventory, list)
        compound.put("Inventory", result as NbtElement)
    }

    fun readInventoryNbt(inventory: Inventory, compound: NbtCompound) {
        val compoundMethod = findMethod(
            inventory.javaClass,
            arrayOf(NbtCompound::class.java)
        )
        if (compoundMethod != null) {
            compoundMethod.invoke(inventory, compound)
            return
        }

        val listMethod = findMethod(
            inventory.javaClass,
            arrayOf(NbtList::class.java)
        ) ?: throw NoSuchMethodException("inventory read NBT")
        listMethod.invoke(inventory, getList(compound, "Inventory"))
    }

    fun writeEnderChestNbt(
        enderChest: EnderChestInventory,
        compound: NbtCompound,
        registryManager: RegistryWrapper.WrapperLookup
    ) {
        val modernMethod = findMethod(
            enderChest.javaClass,
            arrayOf(NbtCompound::class.java, RegistryWrapper.WrapperLookup::class.java)
        )
        if (modernMethod != null) {
            modernMethod.invoke(enderChest, compound, registryManager)
            return
        }

        val compoundMethod = findMethod(
            enderChest.javaClass,
            arrayOf(NbtCompound::class.java)
        )
        if (compoundMethod != null) {
            compoundMethod.invoke(enderChest, compound)
            return
        }

        val listMethod = findMethod(
            enderChest.javaClass,
            arrayOf(RegistryWrapper.WrapperLookup::class.java),
            NbtList::class.java
        ) ?: throw NoSuchMethodException("ender chest write NBT")
        compound.put("EnderItems", listMethod.invoke(enderChest, registryManager) as NbtElement)
    }

    fun readEnderChestNbt(
        enderChest: EnderChestInventory,
        compound: NbtCompound,
        registryManager: RegistryWrapper.WrapperLookup
    ) {
        val compoundMethod = findMethod(
            enderChest.javaClass,
            arrayOf(NbtCompound::class.java)
        )
        if (compoundMethod != null) {
            compoundMethod.invoke(enderChest, compound)
            return
        }

        val listMethod = findMethod(
            enderChest.javaClass,
            arrayOf(NbtList::class.java, RegistryWrapper.WrapperLookup::class.java)
        ) ?: throw NoSuchMethodException("ender chest read NBT")
        listMethod.invoke(enderChest, getList(compound, "EnderItems"), registryManager)
    }

    fun writeStatusEffectNbt(
        effect: StatusEffectInstance,
        registryManager: RegistryWrapper.WrapperLookup
    ): NbtCompound {
        val modernMethod = findMethod(
            StatusEffectInstance::class.java,
            arrayOf(RegistryWrapper.WrapperLookup::class.java),
            NbtCompound::class.java
        )
        if (modernMethod != null) {
            return modernMethod.invoke(effect, registryManager) as NbtCompound
        }

        val legacyMethod = findMethod(
            StatusEffectInstance::class.java,
            emptyArray(),
            NbtCompound::class.java
        ) ?: throw NoSuchMethodException("status effect write NBT")
        return legacyMethod.invoke(effect) as NbtCompound
    }

    fun fromNbtStatusEffect(
        nbt: NbtCompound,
        registryManager: RegistryWrapper.WrapperLookup
    ): StatusEffectInstance? {
        val modernMethod = findMethod(
            StatusEffectInstance::class.java,
            arrayOf(RegistryWrapper.WrapperLookup::class.java, NbtCompound::class.java)
        )
        if (modernMethod != null) {
            return unwrapStatusEffect(modernMethod.invoke(null, registryManager, nbt))
        }

        val legacyMethod = findMethod(
            StatusEffectInstance::class.java,
            arrayOf(NbtCompound::class.java)
        ) ?: throw NoSuchMethodException("status effect read NBT")
        return unwrapStatusEffect(legacyMethod.invoke(null, nbt))
    }

    private fun unwrapStatusEffect(value: Any?): StatusEffectInstance? {
        return when (value) {
            is Optional<*> -> value.orElse(null) as? StatusEffectInstance
            else -> value as? StatusEffectInstance
        }
    }

    private fun findField(type: Class<*>, names: Set<String>): Field? {
        var current: Class<*>? = type
        while (current != null) {
            val field = current.declaredFields.firstOrNull {
                it.name in names && it.type == Float::class.javaPrimitiveType
            }
            if (field != null) {
                field.trySetAccessible()
                return field
            }
            current = current.superclass
        }
        return null
    }

    private fun findMethod(
        type: Class<*>,
        parameterTypes: Array<Class<*>>,
        returnType: Class<*>? = null
    ): Method? {
        var current: Class<*>? = type
        while (current != null) {
            val method = current.declaredMethods.firstOrNull { candidate ->
                candidate.parameterTypes.contentEquals(parameterTypes) &&
                    (returnType == null || returnType == candidate.returnType)
            }
            if (method != null) {
                method.trySetAccessible()
                return method
            }
            current = current.superclass
        }
        return type.methods.firstOrNull { candidate ->
            candidate.parameterTypes.contentEquals(parameterTypes) &&
                (returnType == null || returnType == candidate.returnType)
        }
    }

    private fun findMethod(
        type: Class<*>,
        names: Set<String>,
        parameterTypes: Array<Class<*>>,
        returnType: Class<*>?
    ): Method? {
        var current: Class<*>? = type
        while (current != null) {
            val method = current.declaredMethods.firstOrNull { candidate ->
                candidate.name in names &&
                    candidate.parameterTypes.contentEquals(parameterTypes) &&
                    (returnType == null || returnType == candidate.returnType)
            }
            if (method != null) {
                method.trySetAccessible()
                return method
            }
            current = current.superclass
        }
        return null
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
