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
                        map[stack.unlocalizedName] = determineItemWeight(stack.item)
                    }
                }
            }
            else {
                if (!map.containsKey(item.unlocalizedName)) {
                    map[item.unlocalizedName] = determineItemWeight(item)
                }
            }
        }
    }

    fun determineItemWeight(item: Item): Float {
        var weight = 1f

        if (!isBaseItem(item)) {
            for (ingredient in getIngredients(item)) {
                // TODO: Add the weight of the ingredients
                weight += 1
            }
        }

        return weight
    }

    fun findMatch(item: Item): Float {
        val value = if (JustWeight.itemMap.containsKey(item.unlocalizedName)) {
            JustWeight.itemMap[item.unlocalizedName]!!
        }
        else {
            -1f
        }

        return value
    }

    fun updateValue(item: Item, value: Float) {
        if (JustWeight.itemMap.containsKey(item.unlocalizedName)) {
            JustWeight.itemMap.replace(item.unlocalizedName, value)
        }
    }

    fun isBaseItem(item: Item): Boolean {
        // TODO: Check if the only recipe is circular, if so return true
        return CraftingManager.getRecipe(item.registryName!!)?.ingredients == null || isCircular(item)
    }

    fun isCircular(item: Item): Boolean {
        CraftingManager.getRecipe(item.registryName!!)?.ingredients?.forEach { it ->
            with(it.matchingStacks.getOrNull(0)?.item?.registryName) {
                if (this != null) {
                    CraftingManager.getRecipe(this)?.ingredients?.forEach { sit ->
                        if (sit.matchingStacks.getOrNull(0)?.item == item) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun getIngredients(item: Item): List<Item> {
        val list = mutableListOf<Item>()

        for (recipe in ForgeRegistries.RECIPES) {
            if (recipe.recipeOutput.item == item) {
                for (ingredient in recipe.ingredients) {
                    with(ingredient.matchingStacks.elementAtOrNull(0)) {
                        if (this != null) {
                            list.add(this.item)
                        }
                    }
                }
            }
        }

        return list
    }
}