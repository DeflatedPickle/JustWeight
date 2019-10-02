package com.deflatedpickle.justweight.common.event

import com.deflatedpickle.justweight.api.ICarryWeight
import com.deflatedpickle.justweight.common.capability.CarryWeight
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerContainerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onEntityItemPickupEvent(event: EntityItemPickupEvent) {
        println(event.item.item.translationKey)
    }

    @SubscribeEvent
    fun onItemTossEvent(event: ItemTossEvent) {
    }

    @SubscribeEvent
    fun onPlayerContainerEventClose(event: PlayerContainerEvent.Close) {
    }

    @SubscribeEvent
    fun onAttachCapabilitiesEvent(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` is EntityPlayer) {
            event.addCapability(CarryWeight.NAME, CarryWeight.Provider())
        }
    }

    @SubscribeEvent
    fun onPlayerLoggedInEvent(event: PlayerEvent.PlayerLoggedInEvent) {
        (event.player.getCapability(CarryWeight.Provider.CAPABILITY!!, null) as ICarryWeight).carryWeight = 45f
    }
}