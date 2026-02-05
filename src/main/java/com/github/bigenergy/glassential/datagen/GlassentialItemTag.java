package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;
import java.util.concurrent.CompletableFuture;

// Updated for NeoForge 1.21.5+ API - using BlockTagCopyingItemTagProvider
public class GlassentialItemTag extends BlockTagCopyingItemTagProvider {
    public GlassentialItemTag(PackOutput output,
                              CompletableFuture<HolderLookup.Provider> lookup,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags, Glassential.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Tag copying can be done via copy() method if needed
        // e.g.: copy(BlockTags.EXAMPLE, ItemTags.EXAMPLE);
    }
}
