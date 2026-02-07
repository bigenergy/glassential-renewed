package com.github.bigenergy.glassential.client.model;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlock;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
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

    public OneWayBlockStateModel(BlockStateModel delegate) {
        super(delegate);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state,
                             RandomSource random, List<BlockModelPart> parts) {
        Direction opaqueFace = state.getValue(OneWayGlassBlock.OPAQUE_FACE);

        ModelData data = level.getModelData(pos);
        BlockState mimicState = data.get(OneWayGlassBlockEntity.MIMIC);

        if (mimicState == null || mimicState.isAir()) {
            // No mimic set, use default glass model
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
                // For all other faces, return the glass quads
                for (BlockModelPart part : glassParts) {
                    quads.addAll(part.getQuads(direction));
                }
            }

            return quads;
        }
    }
}
