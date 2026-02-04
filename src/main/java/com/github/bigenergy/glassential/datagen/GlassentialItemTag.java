package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class GlassentialItemTag extends ItemTagsProvider {
    public GlassentialItemTag(PackOutput output,
                              CompletableFuture<HolderLookup.Provider> lookup,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
        super(output, lookup, blockTags, Glassential.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.copy(GlassentialTags.Blocks.DOORS_DYED, GlassentialTags.Items.DOORS_DYED);
        this.copy(GlassentialTags.Blocks.TRAPDOORS_DYED, GlassentialTags.Items.TRAPDOORS_DYED);
        GlassentialTags.Blocks.DOORS_DYED_BY_COLOR.forEach((color, bTag) ->
                this.copy(bTag, GlassentialTags.Items.DOORS_DYED_BY_COLOR.get(color)));
        GlassentialTags.Blocks.TRAPDOORS_DYED_BY_COLOR.forEach((color, bTag) ->
                this.copy(bTag, GlassentialTags.Items.TRAPDOORS_DYED_BY_COLOR.get(color)));
        for (var color : net.minecraft.world.item.DyeColor.values()) {
            this.copy(cDyedBlock(color), cDyedItem(color));
        }
    }

    private static TagKey<Block> cDyedBlock(net.minecraft.world.item.DyeColor color) {
        return BlockTags.create(
                Identifier.fromNamespaceAndPath("c", "dyed/" + color.getName()));
    }

    private static TagKey<net.minecraft.world.item.Item> cDyedItem(net.minecraft.world.item.DyeColor color) {
        return ItemTags.create(
                Identifier.fromNamespaceAndPath("c", "dyed/" + color.getName()));
    }
}
