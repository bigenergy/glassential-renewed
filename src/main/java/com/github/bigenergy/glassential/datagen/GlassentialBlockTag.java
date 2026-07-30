package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import com.github.bigenergy.glassential.init.GlassentialBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class GlassentialBlockTag extends BlockTagsProvider {
    // 1.20.1 Forge: BlockTagsProvider(PackOutput, CompletableFuture<HolderLookup.Provider>, String modId, @Nullable ExistingFileHelper)
    public GlassentialBlockTag(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Glassential.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        // ======== GLASS / TINTED GLASS / PANES / SLABS ========
        tag(BlockTags.IMPERMEABLE).add(
                Stream.of(
                                GlassentialBlocks.GLASSES.stream(),
                                GlassentialBlocks.GLASSES_TINTED.stream(),
                                GlassentialBlocks.PANES.stream(),
                                GlassentialBlocks.SLABS.stream()
                        ).flatMap(s -> s.map(RegistryObject::get))
                        .toArray(Block[]::new)
        );

        // 1.20.1 Forge names: forge:glass / forge:glass/tinted / forge:glass_panes
        // (NeoForge 1.21 renamed these to c:glass_blocks / c:glass_blocks_tinted / c:glass_panes)
        tag(Tags.Blocks.GLASS).add(
                Stream.concat(
                        GlassentialBlocks.GLASSES.stream(),
                        GlassentialBlocks.GLASSES_TINTED.stream()
                ).map(RegistryObject::get).toArray(Block[]::new)
        );

        tag(Tags.Blocks.GLASS_TINTED).add(
                GlassentialBlocks.GLASSES_TINTED.stream().map(RegistryObject::get).toArray(Block[]::new)
        );
        tag(Tags.Blocks.GLASS_PANES).add(
                GlassentialBlocks.PANES.stream().map(RegistryObject::get).toArray(Block[]::new)
        );

        // ======== DOORS ========
        tag(BlockTags.DOORS).add(
                GlassentialBlocks.DOORS.stream().map(RegistryObject::get).toArray(Block[]::new)
        );

        // ======== DOORS DYED ========
        tag(GlassentialTags.Blocks.DOORS_DYED).add(
                GlassentialBlocks.DOORS_DYED.stream().map(RegistryObject::get).toArray(Block[]::new)
        );
        tag(BlockTags.DOORS).add(
                GlassentialBlocks.DOORS_DYED.stream().map(RegistryObject::get).toArray(Block[]::new)
        );
        tag(GlassentialTags.Convention.DYED_BLOCK).add(
                GlassentialBlocks.DOORS_DYED.stream().map(RegistryObject::get).toArray(Block[]::new)
        );

        GlassentialBlocks.DOOR_COLOR.forEach((def, color) -> {
            tag(GlassentialTags.Blocks.DOORS_DYED_BY_COLOR.get(color)).add(def.get());
            tag(GlassentialTags.Convention.dyedBlock(color)).add(def.get());
        });

        // ======== TRAPDOORS ========
        tag(BlockTags.TRAPDOORS).add(
                Stream.concat(
                        GlassentialBlocks.TRAPDOORS.stream(),
                        GlassentialBlocks.TRAPDOORS_DYED.stream()
                ).map(RegistryObject::get).toArray(Block[]::new)
        );

        tag(GlassentialTags.Blocks.TRAPDOORS_DYED).add(
                GlassentialBlocks.TRAPDOORS_DYED.stream().map(RegistryObject::get).toArray(Block[]::new)
        );
        tag(GlassentialTags.Convention.DYED_BLOCK).add(
                GlassentialBlocks.TRAPDOORS_DYED.stream().map(RegistryObject::get).toArray(Block[]::new)
        );

        GlassentialBlocks.TRAPDOOR_COLOR.forEach((def, color) -> {
            tag(GlassentialTags.Blocks.TRAPDOORS_DYED_BY_COLOR.get(color)).add(def.get());
            tag(GlassentialTags.Convention.dyedBlock(color)).add(def.get());
        });
    }
}
