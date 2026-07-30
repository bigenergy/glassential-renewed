package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Registers every data provider owned by Glassential.
 * <p>
 * On the 1.21.1 / NeoForge branch this lives inline in {@code Glassential#gatherData}; here it is kept
 * self-contained so the main mod class does not have to know about datagen. Do <b>not</b> also add a
 * {@code GatherDataEvent} listener in {@code Glassential.java} or the providers will run twice.
 */
@Mod.EventBusSubscriber(modid = Glassential.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GlassentialDataGenerators {

    private GlassentialDataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput out = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        // 1.20.1: LootTableProvider(PackOutput, Set<ResourceLocation> requiredTables, List<SubProviderEntry>)
        // -- no HolderLookup.Provider argument (that was added in 1.21), and SubProviderEntry takes a
        //    Supplier<LootTableSubProvider> rather than a Function<HolderLookup.Provider, ...>.
        gen.addProvider(event.includeServer(), new LootTableProvider(
                out,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        GlassentialBlockLoot::new,
                        LootContextParamSets.BLOCK
                ))
        ));

        GlassentialBlockTag blockTags = gen.addProvider(event.includeServer(),
                new GlassentialBlockTag(out, lookup, helper));

        gen.addProvider(event.includeServer(),
                new GlassentialItemTag(out, lookup, blockTags.contentsGetter(), helper));

        gen.addProvider(event.includeServer(), new GlassentialFluid(out, lookup, helper));
    }
}
