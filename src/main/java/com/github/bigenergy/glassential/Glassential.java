package com.github.bigenergy.glassential;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlockEntity;
import com.github.bigenergy.glassential.client.OneWayModelGeometry;
import com.github.bigenergy.glassential.datagen.GlassentialBlockLoot;
import com.github.bigenergy.glassential.datagen.GlassentialBlockTag;
import com.github.bigenergy.glassential.datagen.GlassentialItemTag;
import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Glassential.MODID)
public class Glassential {
    public static final String MODID = "glassential";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GLASSENTIAL_TAB =
            CREATIVE_MODE_TABS.register("glassential_tab",
                    () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.glassential"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> new ItemStack(GlassentialBlocks.GLASS_DARK_ETHEREAL.get()))
                            .displayItems((parameters, output) -> {
            GlassentialBlocks.ITEMS_FOR_TAB_LIST_FUNC.forEach(registryObject -> output.accept(new ItemStack(registryObject.get())));
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
        modEventBus.addListener(this::commonSetup);
        GlassentialBlocks.BLOCKS.register(modEventBus);
        GlassentialBlocks.ITEMS.register(modEventBus);

        GlassentialBlockEntities.BLOCK_ENTITIES.register(modEventBus);


        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::gatherData);
        //modEventBus.addListener();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

//    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) event.accept(EXAMPLE_BLOCK_ITEM);
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void gatherData(GatherDataEvent e) {
        DataGenerator gen = e.getGenerator();
        PackOutput out = gen.getPackOutput();
        var helper = e.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = e.getLookupProvider();

        gen.addProvider(e.includeServer(), new LootTableProvider(
                out,
                Set.of(), // не включаем ваниллу
                List.of(new LootTableProvider.SubProviderEntry(
                        // передаём lookup в твой провайдер
                        GlassentialBlockLoot::new,
                        LootContextParamSets.BLOCK
                )),
                lookup
        ));

        // 2) Block-tags
        var blockTags = gen.addProvider(e.includeServer(),
                new GlassentialBlockTag(out, lookup, helper));

        // 3) Item-tags (зависят от blockTags)
        gen.addProvider(e.includeServer(),
                new GlassentialItemTag(out, lookup, blockTags.contentsGetter(), helper));

    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code

            event.enqueueWork(() -> {
                // слой рендера стеклянный (как и был)
                ItemBlockRenderTypes.setRenderLayer(GlassentialBlocks.ONE_WAY_GLASS.get(), RenderType.cutoutMipped());

                // === ВАЖНО: цвет проксируется к мимику ===
                BlockColors bc = Minecraft.getInstance().getBlockColors();
                bc.register((state, level, pos, tintIndex) -> {
                    if (level == null || pos == null) return -1; // item / нет мира — не красим
                    var be = level.getBlockEntity(pos);
                    if (be instanceof OneWayGlassBlockEntity ow) {
                        BlockState mimic = ow.getMimic();
                        // спросим цвет у исходного блока-имитатора
                        return Minecraft.getInstance().getBlockColors().getColor(mimic, level, pos, tintIndex);
                    }
                    return -1; // нет тинта
                }, GlassentialBlocks.ONE_WAY_GLASS.get());
            });
        }


        @SubscribeEvent
        public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders e) {
            e.register(ResourceLocation.fromNamespaceAndPath(MODID, "one_way_loader"), new OneWayModelGeometry.Loader());
        }
    }
}
