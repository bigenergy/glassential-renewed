package com.github.bigenergy.glassential.blocks.entity;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
        if (level != null && !level.isClientSide()) {
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
        if (level != null && !level.isClientSide()) {
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("Color", color);
        pTag.putBoolean("EmitLight", emitLight);
        pTag.putBoolean("EmitRedstone", emitRedstone);
        pTag.putBoolean("PassPlayer", passPlayer);
        pTag.putBoolean("PassEntity", passEntity);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        color = pTag.getInt("Color").orElse(0xFFFFFF);
        emitLight = pTag.getBoolean("EmitLight").orElse(false);
        emitRedstone = pTag.getBoolean("EmitRedstone").orElse(false);
        passPlayer = pTag.getBoolean("PassPlayer").orElse(false);
        passEntity = pTag.getBoolean("PassEntity").orElse(false);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        CompoundTag tag = super.getUpdateTag(pRegistries);
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        handleUpdateTag(pkt.getTag(), lookupProvider);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        if (tag.contains("Color").orElse(false)) {
            int oldColor = color;
            color = tag.getInt("Color").orElse(0xFFFFFF);
            emitLight = tag.getBoolean("EmitLight").orElse(false);
            emitRedstone = tag.getBoolean("EmitRedstone").orElse(false);
            passPlayer = tag.getBoolean("PassPlayer").orElse(false);
            passEntity = tag.getBoolean("PassEntity").orElse(false);
            if (level != null && level.isClientSide()) {
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
