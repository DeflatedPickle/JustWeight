package com.deflatedpickle.justweight.common.capability

import com.deflatedpickle.justweight.Reference
import com.deflatedpickle.justweight.api.ICarryWeight
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagFloat
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.*
import java.util.concurrent.Callable
import net.minecraft.util.ResourceLocation

object CarryWeight {
    val NAME = ResourceLocation(Reference.MOD_ID, "carryWeight")

    class Implementation : ICarryWeight {
        private var carryWeight = 0f

        override fun setCarryWeight(weight: Float) {
            this.carryWeight = weight
        }

        override fun getCarryWeight(): Float {
            return this.carryWeight
        }
    }

    class Storage : Capability.IStorage<ICarryWeight> {
        override fun readNBT(capability: Capability<ICarryWeight>?, instance: ICarryWeight?, side: EnumFacing?, nbt: NBTBase?) {
            if (instance is Implementation) {
                instance.carryWeight = (nbt as NBTTagFloat).float
            }
            else {
                throw IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation")
            }
        }

        override fun writeNBT(capability: Capability<ICarryWeight>?, instance: ICarryWeight?, side: EnumFacing?): NBTBase? {
            if (instance != null) {
                return NBTTagFloat(instance.carryWeight)
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