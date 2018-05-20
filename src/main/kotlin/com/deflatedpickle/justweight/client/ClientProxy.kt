package com.deflatedpickle.justweight.client

import com.deflatedpickle.justweight.common.CommonProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import com.deflatedpickle.justweight.client.event.ForgeEventHandler
import net.minecraftforge.common.MinecraftForge



class ClientProxy : CommonProxy() {
    override fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())
    }
}