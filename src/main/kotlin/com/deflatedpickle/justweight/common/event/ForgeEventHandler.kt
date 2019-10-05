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
}