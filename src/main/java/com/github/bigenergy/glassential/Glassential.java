package com.github.bigenergy.glassential;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

@Mod(Glassential.MODID)
public class Glassential {
    public static final String MODID = "glassential";
    public static final Logger LOGGER = LogManager.getLogger("Glassential Renewed");

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> GLASSENTIAL_TAB =
            CREATIVE_MODE_TABS.register("glassential_tab",
                    () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.glassential"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> new ItemStack(GlassentialBlocks.GLASS_DARK_ETHEREAL.get()))
                            .displayItems((parameters, output) -> {
                                GlassentialBlocks.ITEMS_FOR_TAB_LIST_FUNC.forEach(registryObject -> output.accept(new ItemStack(registryObject.get())));
                                output.accept(new ItemStack(GlassentialBlocks.GLASS_PAINTER.get()));
                            }).build());

    public static final RegistryObject<CreativeModeTab> GLASSENTIAL_TAB_FUNCTIONALITY =
            CREATIVE_MODE_TABS.register("glassential_tab_misc",
                    () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.glassential_misc"))
                            .withTabsBefore(GLASSENTIAL_TAB.getId())
                            .icon(() -> new ItemStack(GlassentialBlocks.STONE_GLASS.get()))
                            .displayItems((parameters, output) -> {
                                GlassentialBlocks.ITEMS_FOR_TAB_LIST.forEach(registryObject -> output.accept(new ItemStack(registryObject.get())));
                            }).build());

    public Glassential() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GlassentialBlocks.BLOCKS.register(modEventBus);
        GlassentialBlocks.ITEMS.register(modEventBus);

        GlassentialBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation("glassential", name.toLowerCase(Locale.ROOT));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Starting Glassential Renewed");
    }
}
