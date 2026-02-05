package com.github.bigenergy.glassential.network;

import com.github.bigenergy.glassential.Glassential;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Glassential.MODID)
public class PacketHandler {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ColorUpdatePacket.TYPE,
                ColorUpdatePacket.STREAM_CODEC,
                ColorUpdatePacket::handle
        );

        registrar.playToServer(
                GlassPainterPacket.TYPE,
                GlassPainterPacket.STREAM_CODEC,
                GlassPainterPacket::handle
        );
    }
}
