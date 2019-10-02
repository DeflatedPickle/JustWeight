package com.deflatedpickle.justweight

import com.deflatedpickle.justweight.common.CommonProxy
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, dependencies = Reference.DEPENDENCIES, modLanguageAdapter = Reference.ADAPTER)
object JustWeight {
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    var proxy: CommonProxy? = null

    val log: Logger = LogManager.getLogger(Reference.MOD_ID)

    // <(<Name, Meta>), Weight>
    val itemMap: HashMap<Pair<ResourceLocation, Int>, Float> = HashMap()

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        log.info("Starting preInit.")
        proxy!!.preInit(event)
        log.info("Finished preInit.")
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        log.info("Starting preInit.")
        proxy!!.init(event)
        log.info("Finished preInit.")
    }
}