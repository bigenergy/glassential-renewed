package com.bigenergy.glassential;

import net.fabricmc.api.ModInitializer;

public class Glassential implements ModInitializer {
    
    @Override
    public void onInitialize() {

        Constants.LOG.info("Loading Glassential Renewed - Fabric");
        GlassentialCommon.init();
    }
}
