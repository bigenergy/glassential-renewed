package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GlassentialFluid extends FluidTagsProvider {

    // 1.20.1: FluidTagsProvider(PackOutput, CompletableFuture<HolderLookup.Provider>, String modId,
    //                           @Nullable ExistingFileHelper)
    public GlassentialFluid(PackOutput output,
                            CompletableFuture<HolderLookup.Provider> lookupProvider,
                            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Glassential.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        // Deliberately does NOT call super.addTags(): the vanilla implementation would re-emit
        // minecraft:water / minecraft:lava, which we must not overwrite.
        this.tag(GlassentialTags.Fluids.CLEAR_FLUID_GLASS_FLUIDS_TAG).addTag(FluidTags.WATER);
    }
}
