package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GlassentialItemTag extends ItemTagsProvider {
    public GlassentialItemTag(PackOutput output,
                              CompletableFuture<HolderLookup.Provider> lookup,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                              ExistingFileHelper helper) {
        super(output, lookup, blockTags, Glassential.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Если нужны item-аналоги ванильных тегов — копируй:
        this.copy(BlockTags.IMPERMEABLE, net.minecraft.tags.ItemTags.create(BlockTags.IMPERMEABLE.location()));
        this.copy(Tags.Blocks.GLASS_BLOCKS, net.minecraft.tags.ItemTags.create(Tags.Blocks.GLASS_BLOCKS.location()));
        this.copy(Tags.Blocks.GLASS_PANES, net.minecraft.tags.ItemTags.create(Tags.Blocks.GLASS_PANES.location()));
        this.copy(BlockTags.DOORS, net.minecraft.tags.ItemTags.create(BlockTags.DOORS.location()));
        this.copy(BlockTags.TRAPDOORS, net.minecraft.tags.ItemTags.create(BlockTags.TRAPDOORS.location()));
        this.copy(BlockTags.SLABS, net.minecraft.tags.ItemTags.create(BlockTags.SLABS.location()));

    }
}