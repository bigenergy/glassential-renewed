// com/github/bigenergy/glassential/datagen/GlassentialItemTag.java
package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GlassentialItemTag extends ItemTagsProvider {
    // 1.20.1: ItemTagsProvider(PackOutput, CompletableFuture<HolderLookup.Provider>,
    //                          CompletableFuture<TagsProvider.TagLookup<Block>>, String modId,
    //                          @Nullable ExistingFileHelper)
    public GlassentialItemTag(PackOutput output,
                              CompletableFuture<HolderLookup.Provider> lookup,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                              @Nullable ExistingFileHelper helper) {
        super(output, lookup, blockTags, Glassential.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.copy(GlassentialTags.Blocks.DOORS_DYED, GlassentialTags.Items.DOORS_DYED);
        this.copy(GlassentialTags.Blocks.TRAPDOORS_DYED, GlassentialTags.Items.TRAPDOORS_DYED);
        GlassentialTags.Blocks.DOORS_DYED_BY_COLOR.forEach((color, bTag) ->
                this.copy(bTag, GlassentialTags.Items.DOORS_DYED_BY_COLOR.get(color)));
        GlassentialTags.Blocks.TRAPDOORS_DYED_BY_COLOR.forEach((color, bTag) ->
                this.copy(bTag, GlassentialTags.Items.TRAPDOORS_DYED_BY_COLOR.get(color)));
        for (DyeColor color : DyeColor.values()) {
            this.copy(GlassentialTags.Convention.dyedBlock(color), GlassentialTags.Convention.dyedItem(color));
        }
    }
}
