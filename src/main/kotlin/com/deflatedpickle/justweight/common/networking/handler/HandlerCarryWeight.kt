package com.deflatedpickle.justweight.common.networking.handler

import com.deflatedpickle.justweight.common.capability.CarryWeight
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import com.deflatedpickle.justweight.common.networking.message.MessageCarryWeight as Message
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class HandlerCarryWeight : IMessageHandler<Message, IMessage> {
    override fun onMessage(message: Message, ctx: MessageContext): IMessage? {
        with(Minecraft.getMinecraft().player) {
            if (this.hasCapability(CarryWeight.Provider.CAPABILITY!!, null)) {
                this.getCapability(CarryWeight.Provider.CAPABILITY!!, null)!!.apply {
                    this.max = message.max
                    this.current = message.current
                }
            }
        }

        return null
    }
}