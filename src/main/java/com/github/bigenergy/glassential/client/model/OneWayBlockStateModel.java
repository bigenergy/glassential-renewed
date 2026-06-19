package com.github.bigenergy.glassential.client.model;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Pass-through wrapper around the baked glass model for One Way Glass.
 *
 * <p>In 26.1.x, {@link BlockStateModel#collectParts(RandomSource, List)} is the
 * abstract entry point, but the actual chunk renderer calls the NeoForge-extension
 * default overload {@code collectParts(BlockAndTintGetter, BlockPos, BlockState,
 * RandomSource, List)}. Context-aware models like Fusion's connecting CTM read
 * neighbor information through that 5-arg path. We MUST forward both overloads,
 * otherwise Fusion only ever gets the context-less call and returns its empty
 * fallback model — which renders as untextured white quads.</p>
 *
 * <p>The mimic-on-opaque-face feature is rendered by a separate
 * {@code BlockEntityRenderer} (the dynamic per-position info needed for that lives
 * in the BE, not in this position-agnostic model).</p>
 */
public class OneWayBlockStateModel implements BlockStateModel {

    private final BlockStateModel delegate;

    public OneWayBlockStateModel(BlockStateModel delegate) {
        this.delegate = delegate;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
        delegate.collectParts(random, parts);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state,
                             RandomSource random, List<BlockStateModelPart> parts) {
        // Forward the context-aware overload so Fusion (and any other CTM/contextual
        // model) gets the neighbor data it needs. The default impl in the extension
        // would route back to the 2-arg overload above and lose that context.
        delegate.collectParts(level, pos, state, random, parts);
    }

    @Override
    public Material.Baked particleMaterial() {
        return delegate.particleMaterial();
    }

    @Override
    public int materialFlags() {
        return delegate.materialFlags();
    }
}
