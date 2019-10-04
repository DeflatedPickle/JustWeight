package com.deflatedpickle.justweight.common.event

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.api.ICarryWeight
import com.deflatedpickle.justweight.common.capability.CarryWeight
import com.deflatedpickle.justweight.common.networking.message.MessageCarryWeight
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import kotlin.math.log

class ForgeEventHandler {
    @SubscribeEvent
    fun onLivingUpdateEvent(event: LivingEvent.LivingUpdateEvent) {
        with(event.entity) {
            if (this.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
                this.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!.also {
                    if (it.current > 0) {
                        val weight = 1 - log(it.current.toFloat() + 1f, it.max.toFloat() + 1f)
                        // println("Max: $it.max, Current: $it.current :: Current / Max: ${it.current / it.max} = ${it.current.toFloat() / it.max}, Weight: $weight")
                        this.setVelocity(
                                this.motionX * weight,
                                this.motionY,
                                this.motionZ * weight
                        )
                        this.velocityChanged = true
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onAttachCapabilitiesEvent(event: AttachCapabilitiesEvent<Entity>) {
        // TODO: Also apply the capability to horses, donkeys and mules - weighing them down with armour and inventory
        if (event.`object` is EntityPlayer) {
            event.addCapability(CarryWeight.NAME, CarryWeight.Provider())
        }
    }

    @SubscribeEvent
    fun onPlayerLoggedInEvent(event: PlayerEvent.PlayerLoggedInEvent) {
        if (!event.player.world.isRemote) {
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
                            JustWeight.log.info("The player is carrying ${itemStack.item.registryName}, adding ${JustWeight.itemMap[Pair(itemStack.item.registryName, itemStack.metadata)]!!}")
                            this.incCurrent(JustWeight.itemMap[Pair(itemStack.item.registryName, itemStack.metadata)]!!)
                        }
                    }

                    JustWeight.CHANNEL.sendTo(MessageCarryWeight(this.max, this.current), event.player as EntityPlayerMP)
                }
            }
        }
    }

    @SubscribeEvent
    fun onEntityItemPickupEvent(event: EntityItemPickupEvent) {
        if (!event.entity.world.isRemote) {
            if (event.entityPlayer.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
                event.item.item.also {
                    with(event.entityPlayer.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!) {
                        incCurrent(JustWeight.itemMap[Pair(it.item.registryName, it.metadata)]!! * it.count)
                        JustWeight.CHANNEL.sendTo(MessageCarryWeight(this.max, this.current), event.entityPlayer as EntityPlayerMP)
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onItemTossEvent(event: ItemTossEvent) {
        if (!event.entity.world.isRemote) {
            if (event.player.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
                // TODO: Maybe ignore it if a container is open, so it's not handled twice?
                event.entityItem.item.also {
                    with(event.player.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!) {
                        decCurrent(JustWeight.itemMap[Pair(it.item.registryName, it.metadata)]!! * it.count)
                        JustWeight.CHANNEL.sendTo(MessageCarryWeight(this.max, this.current), event.player as EntityPlayerMP)
                    }
                }
            }
        }
    }
}