package com.github.bigenergy.glassential.blocks.entity;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            // Force light update if emitting light
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
        if (level != null && !level.isClientSide) {
            // Update BlockState LIT property
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
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean getPassPlayer() {
        return passPlayer;
    }

    public void setPassPlayer(boolean passPlayer) {
        this.passPlayer = passPlayer;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean getPassEntity() {
        return passEntity;
    }

    public void setPassEntity(boolean passEntity) {
        this.passEntity = passEntity;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("Color", color);
        pTag.putBoolean("EmitLight", emitLight);
        pTag.putBoolean("EmitRedstone", emitRedstone);
        pTag.putBoolean("PassPlayer", passPlayer);
        pTag.putBoolean("PassEntity", passEntity);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        color = pTag.getInt("Color");
        emitLight = pTag.getBoolean("EmitLight");
        emitRedstone = pTag.getBoolean("EmitRedstone");
        passPlayer = pTag.getBoolean("PassPlayer");
        passEntity = pTag.getBoolean("PassEntity");
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("Color", color);
        tag.putBoolean("EmitLight", emitLight);
        tag.putBoolean("EmitRedstone", emitRedstone);
        tag.putBoolean("PassPlayer", passPlayer);
        tag.putBoolean("PassEntity", passEntity);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            handleUpdateTag(tag);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("Color")) {
            color = tag.getInt("Color");
            emitLight = tag.getBoolean("EmitLight");
            emitRedstone = tag.getBoolean("EmitRedstone");
            passPlayer = tag.getBoolean("PassPlayer");
            passEntity = tag.getBoolean("PassEntity");
            if (level != null && level.isClientSide) {
                // Mark chunk for rebuild
                var mc = Minecraft.getInstance();
                if (mc != null && mc.levelRenderer != null) {
                    mc.levelRenderer.setBlocksDirty(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                                                     worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
                }
            }
        }
    }
}
