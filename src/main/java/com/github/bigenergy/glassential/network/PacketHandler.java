package com.github.bigenergy.glassential.network;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 1.20.1 Forge networking uses {@link SimpleChannel} rather than the 1.21 payload system.
 * Messages are registered once during common setup; ids must stay stable between client
 * and server, so never reorder the {@code messageBuilder} calls below.
 */
@Mod.EventBusSubscriber(modid = Glassential.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketHandler {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Glassential.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();

    private static boolean registered = false;

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
    }

    public static synchronized void register() {
        if (registered) return;
        registered = true;

        int id = 0;

        CHANNEL.messageBuilder(GlassPainterPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(GlassPainterPacket::encode)
                .decoder(GlassPainterPacket::new)
                .consumerMainThread(GlassPainterPacket::handle)
                .add();

        CHANNEL.messageBuilder(ColorUpdatePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ColorUpdatePacket::encode)
                .decoder(ColorUpdatePacket::new)
                .consumerMainThread(ColorUpdatePacket::handle)
                .add();
    }
}
