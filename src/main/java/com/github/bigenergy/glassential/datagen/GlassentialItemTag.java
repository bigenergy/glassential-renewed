package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;

// Simplified for 1.21.4+ API - copy method no longer exists
public class GlassentialItemTag extends ItemTagsProvider {
    public GlassentialItemTag(PackOutput output,
                              CompletableFuture<HolderLookup.Provider> lookup,
                              net.neoforged.neoforge.common.data.BlockTagsProvider blockTags) {
        super(output, lookup, blockTags.contentsGetter(), Glassential.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Tag copying removed in 1.21.4+ - tags need to be defined explicitly
        // or use the newer data-driven tag system
    }
}
