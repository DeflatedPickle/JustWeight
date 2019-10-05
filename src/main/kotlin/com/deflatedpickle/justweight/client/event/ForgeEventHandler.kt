package com.deflatedpickle.justweight.client.event

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.common.capability.CarryWeight
import com.deflatedpickle.justweight.common.util.ItemUtil
import net.minecraft.client.Minecraft
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.max

class ForgeEventHandler {
    @SubscribeEvent
    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {
        with(Minecraft.getMinecraft().player) {
            if (this.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
                this.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!.also {
                    Minecraft.getMinecraft().fontRenderer.drawString(
                            "${TextFormatting.WHITE}Weight: ${it.current}/${it.max}",
                            2f, 2f, 0, true
                    )
                }
            }
        }
    }

    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        val value = max(ItemUtil.findMatch(event.itemStack) * event.itemStack.count, -1)
        val colour = TextFormatting.getValueByName(if (value == -1) "RED" else "GRAY")

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