package com.deflatedpickle.justweight.client.event

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.common.util.ItemUtil
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        val value = ItemUtil.findMatch(event.itemStack.item) * event.itemStack.count
        val colour = TextFormatting.getValueByName("GRAY")

        with(event.toolTip) {
            add("${colour.toString()}${value}g")

            with(CraftingManager.getRecipe(event.itemStack.item.registryName!!)?.ingredients) {
                if (this != null) {
                    add("-----")

                    for (ingredient in this) {
                        for (item in ingredient.matchingStacks) {
                            if (!item.hasSubtypes) {
                                add("${item.displayName} : ${JustWeight.itemMap[item.unlocalizedName]}g")
                            }
                        }
                    }
                }
            }
        }
    }
}