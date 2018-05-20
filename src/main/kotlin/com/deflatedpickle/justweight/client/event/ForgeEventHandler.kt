package com.deflatedpickle.justweight.client.event

import com.deflatedpickle.justweight.common.util.ItemUtil
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        var toolTip = ""
        val value = ItemUtil.findMatch(event.itemStack)
        val colour = TextFormatting.getValueByName("GRAY")

        toolTip += colour.toString()

        toolTip += value

        event.toolTip.add(toolTip)
    }
}