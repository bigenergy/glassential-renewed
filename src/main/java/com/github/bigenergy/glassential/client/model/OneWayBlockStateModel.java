package com.github.bigenergy.glassential.client.model;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlock;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom block state model that shows a mimic block texture on the opaque face
 * of one-way glass, while rendering glass on all other faces.
 */
public class OneWayBlockStateModel extends DelegateBlockStateModel {

    private static final ThreadLocal<Boolean> COLLECTING = ThreadLocal.withInitial(() -> Boolean.FALSE);

    public OneWayBlockStateModel(BlockStateModel delegate) {
        super(delegate);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state,
                             RandomSource random, List<BlockModelPart> parts) {
        if (COLLECTING.get()) {
            // Prevent infinite recursion: mimicModel.collectParts can re-enter
            this.delegate.collectParts(level, pos, state, random, parts);
            return;
        }

        COLLECTING.set(Boolean.TRUE);
        try {
            Direction opaqueFace = state.getValue(OneWayGlassBlock.OPAQUE_FACE);

            ModelData data = level.getModelData(pos);
            BlockState mimicState = data.get(OneWayGlassBlockEntity.MIMIC);

            if (mimicState == null || mimicState.isAir()) {
                // No mimic set, use default glass model
                this.delegate.collectParts(level, pos, state, random, parts);
                return;
            }

            // Never render another One Way Glass as the mimic: its model is also a
            // OneWayBlockStateModel, so doing so re-enters this method. The ThreadLocal
            // COLLECTING flag catches it, but bail out explicitly to avoid the wasted
            // recursive call and to be robust against any reentrancy edge cases.
            // (instanceof covers TintedOneWayGlassBlock too, since it extends OneWayGlassBlock.)
            if (mimicState.getBlock() instanceof OneWayGlassBlock) {
                this.delegate.collectParts(level, pos, state, random, parts);
                return;
            }

            // Get the mimic block's model
            BlockStateModel mimicModel = Minecraft.getInstance()
                    .getModelManager()
                    .getBlockModelShaper()
                    .getBlockModel(mimicState);

            // Collect parts from both models
            List<BlockModelPart> glassParts = new ArrayList<>();
            this.delegate.collectParts(level, pos, state, random, glassParts);

            List<BlockModelPart> mimicParts = new ArrayList<>();
            mimicModel.collectParts(level, pos, mimicState, random, mimicParts);

            // Create a composite part that uses mimic quads for the opaque face
            // and glass quads for all other faces
            parts.add(new CompositeBlockModelPart(glassParts, mimicParts, opaqueFace));
        } finally {
            COLLECTING.set(Boolean.FALSE);
        }
    }

    /**
     * A composite BlockModelPart that returns quads from the mimic model
     * for the opaque face and quads from the glass model for all other faces.
     */
    private record CompositeBlockModelPart(
            List<BlockModelPart> glassParts,
            List<BlockModelPart> mimicParts,
            Direction opaqueFace
    ) implements BlockModelPart {

        @Override
        public List<BakedQuad> getQuads(@Nullable Direction direction) {
            List<BakedQuad> quads = new ArrayList<>();

            if (direction == opaqueFace) {
                // For the opaque face, return the mimic block's quads
                for (BlockModelPart part : mimicParts) {
                    quads.addAll(part.getQuads(direction));
                }
            } else {
                // For all other faces (including null/unculled), return the glass quads
                for (BlockModelPart part : glassParts) {
                    quads.addAll(part.getQuads(direction));
                }
            }

            return quads;
        }

        @Override
        public TextureAtlasSprite particleIcon() {
            if (!glassParts.isEmpty()) {
                return glassParts.get(0).particleIcon();
            }
            if (!mimicParts.isEmpty()) {
                return mimicParts.get(0).particleIcon();
            }
            return null;
        }

        @Override
        public boolean useAmbientOcclusion() {
            if (!glassParts.isEmpty()) {
                return glassParts.get(0).useAmbientOcclusion();
            }
            return true;
        }

        @Override
        public ChunkSectionLayer getRenderType(BlockState state) {
            // Delegate to the glass model's render type (translucent)
            // so the glass faces render with transparency
            if (!glassParts.isEmpty()) {
                return glassParts.get(0).getRenderType(state);
            }
            return BlockModelPart.super.getRenderType(state);
        }
    }
}
