package com.bigenergy.glassential;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Constants.MOD_ID)
public class Glassential {

    public Glassential(IEventBus eventBus) {
        GlassentialCommon.init();
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupClient);
    }

    public void setupCommon(final FMLCommonSetupEvent event) {
    }

    public void setupClient(final FMLClientSetupEvent event) {
    }
}