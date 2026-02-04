package com.github.bigenergy.glassential.client.model;

import com.github.bigenergy.glassential.blocks.OneWayGlassBlock;
import com.github.bigenergy.glassential.blocks.entity.OneWayGlassBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class OneWayBakedModel implements BakedModel /* + фордж-хук ниже */ {
    private final BakedModel glassModel; // откуда берём стеклянные квады


    public OneWayBakedModel(BakedModel glassModel) {
        this.glassModel = glassModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             RandomSource rand, ModelData data, @Nullable RenderType layer) {
        if (state == null || side == null) return Collections.emptyList();

        Direction opaque = state.getValue(OneWayGlassBlock.OPAQUE_FACE);
        if (side != opaque) {
            return glassModel.getQuads(state, side, rand, data, layer);
        }

        BlockState mimic = data.get(OneWayGlassBlockEntity.MIMIC);
        if (mimic == null) {
            return glassModel.getQuads(state, side, rand, data, layer);
        }

        BakedModel mimicModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);

        List<BakedQuad> quads = mimicModel.getQuads(mimic, side, rand, ModelData.EMPTY, layer);
        if (quads.isEmpty()) {
            quads = mimicModel.getQuads(mimic, side, rand, ModelData.EMPTY, null);
        }
        return quads;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState s, @Nullable Direction side, RandomSource r) {
        return Collections.emptyList();
    }


    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return glassModel.getRenderTypes(state, rand, data);
    }
    @Override public boolean useAmbientOcclusion() { return glassModel.useAmbientOcclusion(); }
    @Override public boolean isGui3d()             { return glassModel.isGui3d(); }
    @Override public boolean usesBlockLight()      { return glassModel.usesBlockLight(); }
    @Override public boolean isCustomRenderer()    { return glassModel.isCustomRenderer(); }
    @Override public TextureAtlasSprite getParticleIcon() { return glassModel.getParticleIcon(); }
    @Override public ItemTransforms getTransforms()       { return glassModel.getTransforms(); }
    @Override public ItemOverrides  overrides()           { return glassModel.overrides(); }
}
