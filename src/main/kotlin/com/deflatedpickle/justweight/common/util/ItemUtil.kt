package com.deflatedpickle.justweight.common.util

import com.deflatedpickle.justweight.JustWeight
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraft.util.NonNullList

object ItemUtil {
    fun itemChecker() {
        for (item in ForgeRegistries.ITEMS) {
            // println(item.unlocalizedName + ": " + resultingItems(item))

            if (isBaseItem(item)) {
                JustWeight.log.info("Requires specified weight for: " + item.unlocalizedName)
            }
        }
    }

    fun itemLocator(map: HashMap<String, Float>) {
        for (item in ForgeRegistries.ITEMS) {
            if (item.hasSubtypes) {
                val itemList = NonNullList.create<ItemStack>()
                item.getSubItems(item.creativeTab, itemList)

                for (stack in itemList) {
                    if (!map.containsKey(stack.unlocalizedName)) {
                        map[stack.unlocalizedName] = determineItemWeight(stack)
                    }
                }
            } else {
                val stack = ItemStack(item)

                if (!map.containsKey(stack.unlocalizedName)) {
                    map[stack.unlocalizedName] = determineItemWeight(stack)
                }
            }
        }
    }

    private fun determineItemWeight(stack: ItemStack): Float {
        var weight = 1f

        if (!isBaseItem(stack.item)) {
            for (item in resultingItems(stack.item)) {
                weight += 1
            }
        }

        return weight
    }

    fun findMatch(stack: ItemStack): Float? {
        val value: Float?

        val match = stack.unlocalizedName

        if (JustWeight.itemMap.containsKey(match)) {
            value = JustWeight.itemMap[match]
        } else {
            value = 0f
        }

        return value
    }

    fun updateValue(stack: ItemStack, value: Float) {
        val match = stack.unlocalizedName
        if (JustWeight.itemMap.containsKey(match)) {
            JustWeight.itemMap.replace(match, value)
        }
    }

    fun isBaseItem(item: Item): Boolean {
        return CraftingManager.getRecipe(item.registryName!!)?.ingredients == null
    }

    fun resultingItems(stack: Item): List<ItemStack> {
        val list = mutableListOf<ItemStack>()

        for (recipe in ForgeRegistries.RECIPES) {
            if (recipe.recipeOutput.item == stack) {
                for (ingredient in recipe.ingredients) {
                    for (item in ingredient.matchingStacks) {
                        if (!item.hasSubtypes) {
                            list.add(item)
                        }
                    }
                }
            }
        }

        return list
    }
}