package com.deflatedpickle.justweight.common.event

import com.deflatedpickle.justweight.JustWeight
import com.deflatedpickle.justweight.api.ICarryWeight
import com.deflatedpickle.justweight.common.capability.CarryWeight
import com.deflatedpickle.justweight.common.networking.message.MessageCarryWeight
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

class FMLEventHandler {
    // Server-only
    var inventoryCache = mutableMapOf<UUID, List<List<String>>>()

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

                inventoryCache[event.player.uniqueID] = with(event.player.inventory) {
                    listOf(mainInventory, armorInventory, offHandInventory).map {
                        it.map {
                            item -> "$item"
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onPlayerTickEvent(event: TickEvent.PlayerTickEvent) {
        if (!event.player.world.isRemote) {
            if (inventoryCache.containsKey(event.player.uniqueID)) {
                var inventoryUpdate = false
                with(event.player.inventory) {
                    val inventoryList = listOf(mainInventory, armorInventory, offHandInventory)

                    // Oh, the inefficiency!
                    for ((index, inventory) in inventoryList.withIndex()) {
                        for (item in inventoryCache[event.player.uniqueID]?.get(index)!!.indices) {
                            if ("${inventory[item]}" != inventoryCache[event.player.uniqueID]!![index][item]) {
                                inventoryUpdate = true

                                return@with
                            }
                        }
                    }
                }

                if (inventoryUpdate) {
                    JustWeight.log.info("${event.player.displayNameString}'s inventory was updated, recalculating their carry weight")

                    inventoryCache[event.player.uniqueID] = with(event.player.inventory) {
                        listOf(mainInventory, armorInventory, offHandInventory).map {
                            it.map {
                                item -> "$item"
                            }
                        }
                    }

                    // TODO: Find the difference between the old weight and the new weight then increase/decrease
                    with((event.player.getCapability(CarryWeight.Provider.CAPABILITY!!, null) as ICarryWeight)) {
                        this.current = 0

                        for (inventory in listOf(
                                event.player.inventory.mainInventory,
                                event.player.inventory.offHandInventory,
                                event.player.inventory.armorInventory)
                        ) {
                            for (itemStack in inventory) {
                                this.incCurrent(JustWeight.itemMap[Pair(itemStack.item.registryName, itemStack.metadata)]!! * itemStack.count)
                            }
                        }

                        JustWeight.CHANNEL.sendTo(MessageCarryWeight(this.max, this.current), event.player as EntityPlayerMP)
                    }
                }
            }
        }
    }
}