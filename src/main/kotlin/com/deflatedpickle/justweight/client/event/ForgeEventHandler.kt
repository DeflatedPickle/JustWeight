package com.deflatedpickle.justweight.client.event

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.common.util.ItemUtil
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.max

class ForgeEventHandler {
    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        val value = max(ItemUtil.findMatch(event.itemStack) * event.itemStack.count, -1f)
        val colour = TextFormatting.getValueByName(if (value == -1f) "RED" else "GRAY")

        with(event.toolTip) {
            add("${colour.toString()}${value}g")

            with(CraftingManager.getRecipe(event.itemStack.item.registryName!!)?.ingredients) {
                if (this != null) {
                    add("-----")

                    for (ingredient in this) {
                        with(ingredient.matchingStacks.getOrNull(0)) {
                            if (this != null) {
                                add("${this.displayName} : ${JustWeight.itemMap[Pair(this.item.registryName, this.metadata)]}g")
                            }
                        }
                    }
                }
            }
        }
    }
}