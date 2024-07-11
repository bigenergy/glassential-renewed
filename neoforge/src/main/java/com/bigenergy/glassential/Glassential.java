package com.bigenergy.glassential;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Glassential {

    public Glassential(IEventBus eventBus) {

        Constants.LOG.info("Loading Glassential Renewed - NeoForge");
        GlassentialCommon.init();

    }
}