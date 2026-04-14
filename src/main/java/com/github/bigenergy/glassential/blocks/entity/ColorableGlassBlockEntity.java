package com.github.bigenergy.glassential.blocks.entity;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ColorableGlassBlockEntity extends BlockEntity {
    private int color = 0xFFFFFF; // Default white
    private boolean emitLight = false;
    private boolean emitRedstone = false;
    private boolean passPlayer = false;
    private boolean passEntity = false;

    public ColorableGlassBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(GlassentialBlockEntities.COLORABLE_GLASS.get(), pPos, pBlockState);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            if (emitLight) {
                level.getChunkSource().getLightEngine().checkBlock(worldPosition);
            }
        }
    }

    public boolean getEmitLight() {
        return emitLight;
    }

    public void setEmitLight(boolean emitLight) {
        this.emitLight = emitLight;
        setChanged();
        if (level != null && !level.isClientSide()) {
            BlockState currentState = getBlockState();
            if (currentState.hasProperty(com.github.bigenergy.glassential.blocks.ColorableGlassBlock.LIT)) {
                level.setBlock(worldPosition, currentState.setValue(com.github.bigenergy.glassential.blocks.ColorableGlassBlock.LIT, emitLight), Block.UPDATE_ALL);
            } else if (currentState.hasProperty(com.github.bigenergy.glassential.blocks.ColorableStainedGlassBlock.LIT)) {
                level.setBlock(worldPosition, currentState.setValue(com.github.bigenergy.glassential.blocks.ColorableStainedGlassBlock.LIT, emitLight), Block.UPDATE_ALL);
            }
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean getEmitRedstone() {
        return emitRedstone;
    }

    public void setEmitRedstone(boolean emitRedstone) {
        this.emitRedstone = emitRedstone;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean getPassPlayer() {
        return passPlayer;
    }

    public void setPassPlayer(boolean passPlayer) {
        this.passPlayer = passPlayer;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean getPassEntity() {
        return passEntity;
    }

    public void setPassEntity(boolean passEntity) {
        this.passEntity = passEntity;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void clientRefresh() {
        if (level != null && level.isClientSide()) {
            requestModelDataUpdate();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Color", color);
        output.putBoolean("EmitLight", emitLight);
        output.putBoolean("EmitRedstone", emitRedstone);
        output.putBoolean("PassPlayer", passPlayer);
        output.putBoolean("PassEntity", passEntity);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        color = input.getIntOr("Color", 0xFFFFFF);
        emitLight = input.getBooleanOr("EmitLight", false);
        emitRedstone = input.getBooleanOr("EmitRedstone", false);
        passPlayer = input.getBooleanOr("PassPlayer", false);
        passEntity = input.getBooleanOr("PassEntity", false);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
        clientRefresh();
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput input) {
        super.onDataPacket(connection, input);
        clientRefresh();
    }
}
