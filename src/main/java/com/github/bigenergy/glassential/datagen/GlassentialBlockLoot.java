package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class GlassentialBlockLoot extends BlockLootSubProvider {

    public GlassentialBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);    }

    @Override
    public void generate() {
        BuiltInRegistries.BLOCK.keySet().stream()
                .filter(key -> key.getNamespace().equals(Glassential.MODID))
                .map(key -> BuiltInRegistries.BLOCK.get(key))
                .filter(java.util.Optional::isPresent)
                .map(opt -> opt.get().value())
                .forEach(block -> {
                    if (block instanceof SlabBlock) {
                        add(block, createSlabItemTable(block));
                    } else {
                        dropWhenSilkTouch(block);
                    }
                });
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.keySet().stream()
                .filter(key -> key.getNamespace().equals(Glassential.MODID))
                .map(key -> BuiltInRegistries.BLOCK.get(key))
                .filter(java.util.Optional::isPresent)
                .map(opt -> opt.get().value())
                .toList();
    }
}