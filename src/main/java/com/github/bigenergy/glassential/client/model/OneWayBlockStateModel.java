package com.github.bigenergy.glassential.client.model;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlock;
import com.github.bigenergy.glassential.client.renderer.OneWayMimicRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around the baked glass model for One Way Glass.
 *
 * <p>In 26.1.x, {@link BlockStateModel#collectParts(RandomSource, List)} is the
 * abstract entry point, but the actual chunk renderer calls the NeoForge-extension
 * default overload {@code collectParts(BlockAndTintGetter, BlockPos, BlockState,
 * RandomSource, List)}. Context-aware models like Fusion's connecting CTM read
 * neighbor information through that 5-arg path. We MUST forward both overloads,
 * otherwise Fusion only ever gets the context-less call and returns its empty
 * fallback model — which renders as untextured white quads.</p>
 *
 * <p>The mimic on the opaque face is drawn by {@code OneWayGlassBlockEntityRenderer},
 * which submits the glass as a moving block inside a {@link OneWayMimicRenderState}.
 * NeoForge's block renderer hands that state back to us as the level, and for it we
 * emit only the mimic's quads facing out of the opaque face. A single outward quad is
 * culled when seen from behind, so the face is solid from outside and see-through
 * from inside the glass.</p>
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
        if (level instanceof OneWayMimicRenderState mimicRender) {
            collectMimicFace(mimicRender, pos, random, parts);
            return;
        }
        // Forward the context-aware overload so Fusion (and any other CTM/contextual
        // model) gets the neighbor data it needs. The default impl in the extension
        // would route back to the 2-arg overload above and lose that context.
        delegate.collectParts(level, pos, state, random, parts);
    }

    @Override
    public boolean hasMaterialFlag(BlockAndTintGetter level, BlockPos pos, BlockState state, int flag) {
        // Picks the solid or translucent pass for the submitted mimic: that has to follow
        // the mimic's own model, not the glass it stands in for.
        if (level instanceof OneWayMimicRenderState mimicRender) {
            return modelOf(mimicRender.mimic).hasMaterialFlag(level, pos, mimicRender.mimic, flag);
        }
        return BlockStateModel.super.hasMaterialFlag(level, pos, state, flag);
    }

    @Override
    public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (level instanceof OneWayMimicRenderState mimicRender) {
            return modelOf(mimicRender.mimic).materialFlags(level, pos, mimicRender.mimic);
        }
        return BlockStateModel.super.materialFlags(level, pos, state);
    }

    @Override
    public Material.Baked particleMaterial() {
        return delegate.particleMaterial();
    }

    @Override
    public int materialFlags() {
        return delegate.materialFlags();
    }

    private static void collectMimicFace(OneWayMimicRenderState render, BlockPos pos,
                                         RandomSource random, List<BlockStateModelPart> parts) {
        // A One Way Glass mimic resolves back to this model and would recurse forever
        // (the StackOverflowError from issue #53).
        if (render.mimic.getBlock() instanceof OneWayGlassBlock) {
            return;
        }
        List<BlockStateModelPart> mimicParts = new ArrayList<>();
        modelOf(render.mimic).collectParts(render, pos, render.mimic, random, mimicParts);
        for (BlockStateModelPart part : mimicParts) {
            parts.add(new SingleFacePart(part, render.opaqueFace));
        }
    }

    private static BlockStateModel modelOf(BlockState state) {
        return Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
    }

    /** A mimic part cut down to the quads of a single face; all other quads are dropped. */
    private record SingleFacePart(BlockStateModelPart delegate, Direction face) implements BlockStateModelPart {

        @Override
        public List<BakedQuad> getQuads(@Nullable Direction direction) {
            return direction == face ? delegate.getQuads(direction) : List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return delegate.useAmbientOcclusion();
        }

        @Override
        public TriState ambientOcclusion() {
            return delegate.ambientOcclusion();
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
}
