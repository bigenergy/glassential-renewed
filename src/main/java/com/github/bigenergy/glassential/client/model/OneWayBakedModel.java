package com.github.bigenergy.glassential.client.model;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlock;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Custom baked model that wraps a glass model and shows a mimic block on one face.
 */
public class OneWayBakedModel extends BakedModelWrapper<BakedModel> {

    public OneWayBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                     RandomSource rand, ModelData data, @Nullable RenderType layer) {
        if (state == null || side == null) return Collections.emptyList();

        Direction opaque = state.getValue(OneWayGlassBlock.OPAQUE_FACE);
        if (side != opaque) {
            return super.getQuads(state, side, rand, data, layer);
        }

        BlockState mimic = data.get(OneWayGlassBlockEntity.MIMIC);
        if (mimic == null) {
            return super.getQuads(state, side, rand, data, layer);
        }

        BakedModel mimicModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);

        List<BakedQuad> quads = mimicModel.getQuads(mimic, side, rand, ModelData.EMPTY, layer);
        if (quads.isEmpty()) {
            quads = mimicModel.getQuads(mimic, side, rand, ModelData.EMPTY, null);
        }
        return quads;
    }
}
