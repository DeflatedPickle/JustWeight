package com.deflatedpickle.justweight

import com.deflatedpickle.justweight.common.CommonProxy
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import net.minecraftforge.fml.common.SidedProxy

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS, dependencies = Reference.DEPENDENCIES, modLanguageAdapter = Reference.ADAPTER)
object JustWeight {
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    var proxy: CommonProxy? = null

    val log: Logger = LogManager.getLogger(Reference.MOD_ID)

    val itemMap: HashMap<String, Float> = HashMap()

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        log.info("Starting preInit.")
        proxy!!.init(event)
        log.info("Finished preInit.")
    }
}