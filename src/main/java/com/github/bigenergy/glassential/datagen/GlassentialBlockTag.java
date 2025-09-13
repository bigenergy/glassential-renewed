package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GlassentialBlockTag extends BlockTagsProvider {
    public GlassentialBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Glassential.MODID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // glasses
        tag(BlockTags.IMPERMEABLE)
                .add(GlassentialBlocks.GLASSES.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));

        tag(Tags.Blocks.GLASS_BLOCKS)
                .add(GlassentialBlocks.GLASSES.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));

        // panes
        tag(BlockTags.IMPERMEABLE)
                .add(GlassentialBlocks.PANES.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));

        tag(Tags.Blocks.GLASS_PANES)
                .add(GlassentialBlocks.PANES.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));

        // doors
        tag(BlockTags.DOORS)
                .add(GlassentialBlocks.DOORS.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));

        // trapdoors
        tag(BlockTags.TRAPDOORS)
                .add(GlassentialBlocks.TRAPDOORS.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));
        tag(BlockTags.IMPERMEABLE)
                .add(GlassentialBlocks.TRAPDOORS.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));

        // slabs
        tag(BlockTags.SLABS)
                .add(GlassentialBlocks.SLABS.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));
        tag(BlockTags.IMPERMEABLE)
                .add(GlassentialBlocks.SLABS.stream()
                        .map(DeferredHolder::get) // DeferredBlock<Block> → Block
                        .toArray(Block[]::new));
    }
}
