package com.deflatedpickle.justweight.common.capability

import com.deflatedpickle.justweight.Reference
import com.deflatedpickle.justweight.api.ICarryWeight
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagIntArray
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import java.util.concurrent.Callable
import kotlin.math.max
import kotlin.math.min

object CarryWeight {
    val NAME = ResourceLocation(Reference.MOD_ID, "carryWeight")

    class Implementation : ICarryWeight {
        private var max = 0
        private var current = 0

        override fun setMax(value: Int) { this.max = value }
        override fun getMax(): Int = this.max

        override fun incCurrent(value: Int) { this.current = min(this.current + value, this.max) }
        override fun decCurrent(value: Int) { this.current = max(this.current - value, 0) }
        override fun setCurrent(value: Int) { this.current = min(value, this.max) }
        override fun getCurrent(): Int = this.current
    }

    class Storage : Capability.IStorage<ICarryWeight> {
        override fun readNBT(capability: Capability<ICarryWeight>?, instance: ICarryWeight?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                with(nbt as NBTTagIntArray) {
                    instance.max = this.intArray[0]
                    instance.current = this.intArray[1]
                }
            }
            else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<ICarryWeight>?, instance: ICarryWeight?, side: EnumFacing?): NBTBase? {
            if (instance != null) {
                return NBTTagIntArray(intArrayOf(instance.max, instance.current))
            }
            return null
        }
    }

    class Factory : Callable<ICarryWeight> {
        override fun call(): ICarryWeight {
            return Implementation()
        }
    }

    class Provider : ICapabilitySerializable<NBTBase> {
        companion object {
            @CapabilityInject(ICarryWeight::class)
            @JvmStatic
            var CAPABILITY: Capability<ICarryWeight>? = null
        }

        val INSTANCE = CAPABILITY?.defaultInstance

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            return capability == CAPABILITY
        }

        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            return if (capability == CAPABILITY) CAPABILITY!!.cast(this.INSTANCE) else null
        }

        override fun serializeNBT(): NBTBase {
            return CAPABILITY!!.storage.writeNBT(CAPABILITY, this.INSTANCE, null)!!
        }

        override fun deserializeNBT(nbt: NBTBase) {
            CAPABILITY!!.storage.readNBT(CAPABILITY, this.INSTANCE, null, nbt)
        }
    }

    fun register() {
        CapabilityManager.INSTANCE.register(ICarryWeight::class.java, Storage(), Factory())
    }
}