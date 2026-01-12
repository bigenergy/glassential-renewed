package com.github.bigenergy.glassential;

import com.github.bigenergy.glassential.datagen.GlassentialBlockLoot;
import com.github.bigenergy.glassential.datagen.GlassentialBlockTag;
import com.github.bigenergy.glassential.datagen.GlassentialFluid;
import com.github.bigenergy.glassential.datagen.GlassentialItemTag;
import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import com.github.bigenergy.glassential.network.GlassPainterPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Glassential.MODID)
public class Glassential {
    public static final String MODID = "glassential";
    public static final Logger LOGGER = LogManager.getLogger("Glassential Renewed");

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GLASSENTIAL_TAB =
            CREATIVE_MODE_TABS.register("glassential_tab",
                    () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.glassential"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> new ItemStack(GlassentialBlocks.GLASS_DARK_ETHEREAL.get()))
                            .displayItems((parameters, output) -> {
            GlassentialBlocks.ITEMS_FOR_TAB_LIST_FUNC.forEach(registryObject -> output.accept(new ItemStack(registryObject.get())));
            output.accept(new ItemStack(GlassentialBlocks.GLASS_PAINTER.get()));
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GLASSENTIAL_TAB_FUNCTIONALITY =
            CREATIVE_MODE_TABS.register("glassential_tab_misc",
                    () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.glassential_misc"))
                            .withTabsBefore(GLASSENTIAL_TAB.getId())
                            .icon(() -> new ItemStack(GlassentialBlocks.STONE_GLASS.get()))
                            .displayItems((parameters, output) -> {
                                GlassentialBlocks.ITEMS_FOR_TAB_LIST.forEach(registryObject -> output.accept(new ItemStack(registryObject.get())));
                            }).build());


    public Glassential(IEventBus modEventBus, ModContainer modContainer) {
        //modEventBus.addListener(this::commonSetup);
        GlassentialBlocks.BLOCKS.register(modEventBus);
        GlassentialBlocks.ITEMS.register(modEventBus);

        GlassentialBlockEntities.BLOCK_ENTITIES.register(modEventBus);


        CREATIVE_MODE_TABS.register(modEventBus);

        // Register network packets
        modEventBus.addListener(this::registerPackets);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::gatherData);
        //modEventBus.addListener();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerPackets(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToServer(
            GlassPainterPacket.TYPE,
            GlassPainterPacket.STREAM_CODEC,
            GlassPainterPacket::handle
        );
    }

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath("glassential", name.toLowerCase(Locale.ROOT));
    }

//    private void commonSetup(final FMLCommonSetupEvent event) {
//        // Some common setup code
//        LOGGER.info("Starting Glassential Renewed");
//
////        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
////
////        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
////
////        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
//    }

//    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Starting Glassential Renewed");
    }

    private void gatherData(GatherDataEvent e) {
        DataGenerator gen = e.getGenerator();
        PackOutput out = gen.getPackOutput();
        var helper = e.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = e.getLookupProvider();

        gen.addProvider(e.includeServer(), new LootTableProvider(
                out,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        GlassentialBlockLoot::new,
                        LootContextParamSets.BLOCK
                )),
                lookup
        ));

        var blockTags = gen.addProvider(e.includeServer(),
                new GlassentialBlockTag(out, lookup, helper));

        gen.addProvider(e.includeServer(),
                new GlassentialItemTag(out, lookup, blockTags.contentsGetter(), helper));
        gen.addProvider(e.includeServer(), new GlassentialFluid(out, lookup, helper));

    }


}
