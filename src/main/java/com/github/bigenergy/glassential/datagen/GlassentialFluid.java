package com.github.bigenergy.glassential.datagen;

import com.github.bigenergy.glassential.Glassential;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class GlassentialFluid extends FluidTagsProvider {

    public GlassentialFluid(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Glassential.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(GlassentialTags.Fluids.CLEAR_FLUID_GLASS_FLUIDS_TAG).addTag(FluidTags.WATER);
    }
}
