package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class GlassentialBlockLoot extends BlockLootSubProvider {

    // 1.20.1: BlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures)
    // (the HolderLookup.Provider parameter only exists from 1.21 onwards)
    public GlassentialBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Glassential.MODID))
                .map(Map.Entry::getValue).forEach(block -> {
                    if (block instanceof SlabBlock) {
                        add(block, createSlabItemTable(block));
                    } else {
                        dropWhenSilkTouch(block);
                    }
                });
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(Glassential.MODID))
                .map(Map.Entry::getValue)
                .toList();
    }
}
