package com.deflatedpickle.justweight.common.util

import com.deflatedpickle.justweight.JustWeight
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraft.util.NonNullList

object ItemUtil {
    fun itemLocator(map: HashMap<String, Float>) {
        for (item in ForgeRegistries.ITEMS) {
            if (item.hasSubtypes) {
                val itemList = NonNullList.create<ItemStack>()
                item.getSubItems(item.creativeTab!!, itemList)

                for (stack in itemList) {
                    if (!map.containsKey(stack.unlocalizedName)) {
                        map[stack.unlocalizedName] = determinItemValue(stack)
                    }
                }
            } else {
                val stack = ItemStack(item)

                if (!map.containsKey(stack.unlocalizedName)) {
                    map[stack.unlocalizedName] = determinItemValue(stack)
                }
            }
        }
    }

    private fun determinItemValue(stack: ItemStack): Float {
        return 0f
    }

    fun findMatch(stack: ItemStack): Float? {
        val value: Float?

        val match = stack.unlocalizedName

        if (JustWeight.itemMap.containsKey(match)) {
            value = JustWeight.itemMap[match]
        } else {
            value = 0.0f
        }

        return value
    }

    fun updateValue(stack: ItemStack, value: Float?) {
        val match = stack.unlocalizedName
        if (JustWeight.itemMap.containsKey(match)) {
            JustWeight.itemMap.replace(match, value)
        }
    }
}