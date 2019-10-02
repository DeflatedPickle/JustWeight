package com.deflatedpickle.justweight.common.util

import com.deflatedpickle.justweight.JustWeight
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation

object ItemUtil {
    fun itemChecker() {
        for (item in ForgeRegistries.ITEMS) {
            // println(item.unlocalizedName + ": " + resultingItems(item))

            if (isBaseItem(ItemStack(item))) {
                JustWeight.log.info("Requires specified weight for: " + item.translationKey)
            }
        }
    }

    fun itemLocator(map: HashMap<Pair<ResourceLocation, Int>, Int>) {
        for (item in ForgeRegistries.ITEMS) {
            if (item.hasSubtypes) {
                val itemList = NonNullList.create<ItemStack>()
                item.getSubItems(item.creativeTab, itemList)

                for (stack in itemList) {
                    val pair = Pair(stack.item.registryName!!, stack.metadata)
                    if (!map.containsKey(pair)) {
                        map[pair] = determineItemWeight(stack)
                    }
                }
            }
            else {
                val pair = Pair(item.registryName!!, 0)
                if (!map.containsKey(pair)) {
                    map[pair] = determineItemWeight(ItemStack(item))
                }
            }
        }
    }

    fun determineItemWeight(stack: ItemStack): Int {
        JustWeight.log.info("Working out the weight for ${stack.translationKey} with the meta ${stack.metadata}")
        var weight = 1

        if (!isBaseItem(stack)) {
            for (ingredient in getIngredients(stack)) {
                JustWeight.log.info("Adding the ingredient ${ingredient.translationKey} with the meta ${ingredient.metadata}")
                // TODO: Add the weight of the ingredients
                val pair = Pair(stack.item.registryName!!, stack.metadata)
                weight += if (JustWeight.itemMap.containsKey(pair)) {
                    JustWeight.itemMap[pair]!!
                }
                else {
                    determineItemWeight(ingredient).apply {
                        JustWeight.itemMap[pair] = this
                    }
                }
            }
        }

        return weight
    }

    fun findMatch(stack: ItemStack): Int {
        val match = Pair(stack.item.registryName!!, stack.metadata)
        val value = if (JustWeight.itemMap.containsKey(match)) {
            JustWeight.itemMap[match]!!
        }
        else {
            -1
        }

        return value
    }

    fun updateValue(stack: ItemStack, value: Int) {
        val match = Pair(stack.item.registryName!!, stack.metadata)
        if (JustWeight.itemMap.containsKey(match)) {
            JustWeight.itemMap.replace(match, value)
        }
    }

    fun isBaseItem(stack: ItemStack): Boolean {
        return CraftingManager.getRecipe(stack.item.registryName!!)?.ingredients == null || isCircular(stack)
    }

    fun isCircular(stack: ItemStack): Boolean {
        // println("stack = ${stack.item.translationKey}, ${stack.metadata}")
        // CraftingManager.getRecipe(stack.item.registryName!!)?.ingredients?.forEach { stackRecipe ->
        //     with(stackRecipe.matchingStacks.getOrNull(stack.metadata)?.item?.registryName) {
        //         println("${stack.item.translationKey} : $this")
        //         if (this != null) {
        //             with(CraftingManager.getRecipe(this)) {
        //                 if (this != null) {
        //                     ingredients.forEach { compoundRecipe ->
        //                         compoundRecipe.matchingStacks.forEach {
        //                             println("${it.item.translationKey}, ${stack.item.translationKey}, ${it.item == stack.item}")
        //                             val itemList = NonNullList.create<ItemStack>()
        //                             stack.item.getSubItems(stack.item.creativeTab, itemList)
        //                             for (i in itemList) {
        //                                 println("sub item ${i.translationKey} --- ${stack.translationKey} ||| ${i == stack}")
        //                                 if (i.item == stack.item) {
        //                                     return true
        //                                 }
        //                             }
        //                         }
        //                     }
        //                 }
        //                 else {
        //                     return true
        //                 }
        //             }
        //         }
        //     }
        // }

        return true
    }

    fun getIngredients(stack: ItemStack): List<ItemStack> {
        val list = mutableListOf<ItemStack>()

        for (recipe in ForgeRegistries.RECIPES) {
            if (recipe.recipeOutput.item == stack.item) {
                for (ingredient in recipe.ingredients) {
                    with(ingredient.matchingStacks.elementAtOrNull(0)) {
                        if (this != null) {
                            list.add(this)
                        }
                    }
                }
            }
        }

        return list
    }
}