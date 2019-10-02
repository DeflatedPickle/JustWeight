package com.deflatedpickle.justweight.common.config;

import com.deflatedpickle.justweight.Reference;
import com.deflatedpickle.justweight.common.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
@Config(modid = Reference.MOD_ID, name = Reference.CONFIG_GENERAL, category = Configuration.CATEGORY_GENERAL)
@Config.LangKey("config.justweight.general")
public class ConfigHandler {
    @Config.Name("Item Config")
    @Config.Comment("Configure the weights of all the items")
    @Config.LangKey("config.justweight.configItem")
    public static final HashMap<String, Float> itemMap = new HashMap<>();

    static {
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            for (Item item : ForgeRegistries.ITEMS) {
                // itemMap.put(item.getTranslationKey(), ItemUtil.INSTANCE.determineItemWeight(new ItemStack(item)));
            }
        }
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
