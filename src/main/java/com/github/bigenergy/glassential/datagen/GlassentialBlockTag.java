package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class GlassentialBlockTag extends BlockTagsProvider {
    public GlassentialBlockTag(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Glassential.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // ======== GLASS / TINTED GLASS / PANES / SLABS ========
        tag(BlockTags.IMPERMEABLE).add(
                Stream.of(
                                GlassentialBlocks.GLASSES.stream(),
                                GlassentialBlocks.GLASSES_TINTED.stream(),
                                GlassentialBlocks.PANES.stream(),
                                GlassentialBlocks.SLABS.stream()
                        ).flatMap(s -> s.map(DeferredHolder::getKey))
                        .toArray(ResourceKey[]::new)
        );

        tag(Tags.Blocks.GLASS_BLOCKS).add(
                Stream.concat(
                        GlassentialBlocks.GLASSES.stream(),
                        GlassentialBlocks.GLASSES_TINTED.stream()
                ).map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );

        tag(Tags.Blocks.GLASS_BLOCKS_TINTED).add(
                GlassentialBlocks.GLASSES_TINTED.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );
        tag(Tags.Blocks.GLASS_PANES).add(
                GlassentialBlocks.PANES.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );

        // ======== DOORS ========
        tag(BlockTags.DOORS).add(
                GlassentialBlocks.DOORS.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );

        // ======== DOORS DYED ========
        tag(GlassentialTags.Blocks.DOORS_DYED).add(
                GlassentialBlocks.DOORS_DYED.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );
        tag(BlockTags.DOORS).add(
                GlassentialBlocks.DOORS_DYED.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );
        tag(Tags.Blocks.DYED).add(
                GlassentialBlocks.DOORS_DYED.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );

        GlassentialBlocks.DOOR_COLOR.forEach((def, color) -> {
            tag(GlassentialTags.Blocks.DOORS_DYED_BY_COLOR.get(color)).add(def.getKey());
            tag(cDyedBlock(color)).add(def.getKey());
        });

        // ======== TRAPDOORS ========
        tag(BlockTags.TRAPDOORS).add(
                Stream.concat(
                        GlassentialBlocks.TRAPDOORS.stream(),
                        GlassentialBlocks.TRAPDOORS_DYED.stream()
                ).map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );

        tag(GlassentialTags.Blocks.TRAPDOORS_DYED).add(
                GlassentialBlocks.TRAPDOORS_DYED.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );
        tag(Tags.Blocks.DYED).add(
                GlassentialBlocks.TRAPDOORS_DYED.stream().map(DeferredHolder::getKey).toArray(ResourceKey[]::new)
        );

        GlassentialBlocks.TRAPDOOR_COLOR.forEach((def, color) -> {
            tag(GlassentialTags.Blocks.TRAPDOORS_DYED_BY_COLOR.get(color)).add(def.getKey());
            tag(cDyedBlock(color)).add(def.getKey());
        });
    }

    private static DyeColor parseColorFromPath(String path) {
        for (DyeColor c : DyeColor.values()) {
            if (path.startsWith(c.getName() + "_")) return c;
        }
        return null;
    }

    private static TagKey<Block> cDyedBlock(DyeColor color) {
        return BlockTags.create(Identifier.fromNamespaceAndPath("c", "dyed/" + color.getName()));
    }
}
