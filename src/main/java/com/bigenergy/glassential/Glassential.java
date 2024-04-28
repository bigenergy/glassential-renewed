package com.bigenergy.glassential;

import com.bigenergy.glassential.init.GBlocks;
import com.bigenergy.glassential.init.GItems;
import com.bigenergy.glassential.tab.GlassentialCreativeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Glassential.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Glassential.MODID)
public class Glassential {
	public static final String MODID = "glassential";

	public static final Logger LOGGER = LogManager.getLogger();

	public Glassential() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		GBlocks.register(bus);
		GItems.register(bus);

		bus.addListener(GlassentialCreativeTab::registerTab);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		LOGGER.info("Starting Glassential Renewed...");
	}
}