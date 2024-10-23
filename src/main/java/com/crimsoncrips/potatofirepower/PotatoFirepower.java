package com.crimsoncrips.potatofirepower;

import com.crimsoncrips.potatofirepower.config.PPConfig;
import com.crimsoncrips.potatofirepower.config.PPConfigHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PotatoFirepower.MODID)
public class PotatoFirepower {

    public static final String MODID = "potatofirepower";
    public PotatoFirepower() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onModConfigEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PPConfigHolder.POTATO_POWER_SPEC, "potatopower.toml");
    }

    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == PPConfigHolder.POTATO_POWER_SPEC) {
            PPConfig.bake();
        }
    }
    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }
}
