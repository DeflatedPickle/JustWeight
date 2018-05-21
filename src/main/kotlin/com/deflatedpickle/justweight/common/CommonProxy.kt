package com.deflatedpickle.justweight.common

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.common.util.ItemUtil
import net.minecraftforge.fml.common.event.FMLInitializationEvent

open class CommonProxy {
    open fun init(event: FMLInitializationEvent) {
        ItemUtil.itemLocator(JustWeight.itemMap)
    }
}