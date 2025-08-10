package com.bigenergy.glassential;


import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class Glassential {

    public Glassential(IEventBus busneo) {

        Constants.LOG.info("Loading Glassential Renewed - NeoForge");
        GlassentialCommon.init();

        IEventBus bus = NeoForge.EVENT_BUS;
        //bus.addListener(EventPriority.HIGHEST, ClientFogHandler::onRenderFog);
    }
}