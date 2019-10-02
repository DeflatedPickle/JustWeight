package com.deflatedpickle.justweight.common.event

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.api.ICarryWeight
import com.deflatedpickle.justweight.common.capability.CarryWeight
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent

class ForgeEventHandler {
    @SubscribeEvent
    fun onEntityItemPickupEvent(event: EntityItemPickupEvent) {
        if (event.entityPlayer.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
            with(event.item.item) {
                // TODO: Apply slowness with the more items held, then nausea, etc
                event.entityPlayer.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!.incCurrent(JustWeight.itemMap[Pair(this.item.registryName, this.metadata)]!! * this.count)
            }
        }
    }

    @SubscribeEvent
    fun onItemTossEvent(event: ItemTossEvent) {
        if (event.player.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
            // TODO: Maybe ignore it if a container is open, so it's not handled twice?
            with(event.entityItem.item) {
                event.player.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!.decCurrent(JustWeight.itemMap[Pair(this.item.registryName, this.metadata)]!! * this.count)
            }
        }
    }

    @SubscribeEvent
    fun onAttachCapabilitiesEvent(event: AttachCapabilitiesEvent<Entity>) {
        if (event.`object` is EntityPlayer) {
            event.addCapability(CarryWeight.NAME, CarryWeight.Provider())
        }
    }

    @SubscribeEvent
    fun onPlayerLoggedInEvent(event: PlayerEvent.PlayerLoggedInEvent) {
        if (event.player.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
            with((event.player.getCapability(CarryWeight.Provider.CAPABILITY!!, null) as ICarryWeight)) {
                // TODO: Make the maximum configurable
                this.max = 4500

                for (inventory in listOf(
                        event.player.inventory.mainInventory,
                        event.player.inventory.offHandInventory,
                        event.player.inventory.armorInventory)
                ) {
                    for (itemStack in inventory) {
                        this.incCurrent(JustWeight.itemMap[Pair(itemStack.item.registryName, itemStack.metadata)]!!)
                    }
                }
            }
        }
    }
}