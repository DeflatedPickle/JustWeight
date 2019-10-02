package com.deflatedpickle.justweight.common

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.common.capability.CarryWeight
import com.deflatedpickle.justweight.common.event.ForgeEventHandler
import com.deflatedpickle.justweight.common.util.ItemUtil
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

open class CommonProxy {
    open fun preInit(event: FMLPreInitializationEvent) {
        CarryWeight.register()
    }

    open fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler())

        ItemUtil.itemLocator(JustWeight.itemMap)
    }
}