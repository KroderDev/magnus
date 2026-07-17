package dev.kroder.magnus.infrastructure.fabric

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class NbtCompatTest {

    @Test
    fun `reads list data without relying on mapped method names`() {
        val item = NbtCompound()
        item.putString("id", "minecraft:diamond")
        val list = NbtList()
        list.add(item)
        val root = NbtCompound()
        root.put("Inventory", list)

        val restoredList = NbtCompat.getList(root, "Inventory")

        assertEquals(1, NbtCompat.listSize(restoredList))
        assertNotNull(NbtCompat.getCompound(restoredList, 0).get("id"))
    }

    @Test
    fun `returns an empty list for a missing key`() {
        assertEquals(0, NbtCompat.listSize(NbtCompat.getList(NbtCompound(), "Inventory")))
    }
}
