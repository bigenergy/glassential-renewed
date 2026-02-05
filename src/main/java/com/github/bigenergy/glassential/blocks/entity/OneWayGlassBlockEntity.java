package com.github.bigenergy.glassential.blocks.entity;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class OneWayGlassBlockEntity extends BlockEntity {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic = Blocks.GLASS.defaultBlockState();

    public OneWayGlassBlockEntity(BlockPos pos, BlockState state) {
        super(GlassentialBlockEntities.ONE_WAY_GLASS.get(), pos, state);
    }

    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(MIMIC, mimic).build();
    }

    public void setMimic(BlockState state) {
        this.mimic = state;
        setChanged();
        if (level instanceof ServerLevel server) {
            server.getChunkSource().blockChanged(worldPosition);
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
        requestModelDataUpdate();
    }

    public BlockState getMimic() {
        return mimic;
    }

    public void clientRefresh() {
        requestModelDataUpdate();
        if (level != null && Minecraft.getInstance().levelRenderer != null) {
            Minecraft.getInstance().levelRenderer.blockChanged(
                    level,
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_CLIENTS
            );
        }
    }
}
