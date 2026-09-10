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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
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
            applyLitProperty(emitLight);
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

    /**
     * Applies a whole brush configuration at once.
     *
     * <p>Each individual setter broadcasts on its own, so calling all five in a row
     * sends five block updates for one click. This sends one. It is also the only
     * entry point that is safe to call from an item: painting has to stay
     * server-authoritative, or the colour exists solely on the painter's client and
     * no other player is ever told about it.
     */
    public void applySettings(int color, boolean emitLight, boolean emitRedstone,
                              boolean passPlayer, boolean passEntity) {
        this.color = color;
        this.emitLight = emitLight;
        this.emitRedstone = emitRedstone;
        this.passPlayer = passPlayer;
        this.passEntity = passEntity;
        setChanged();

        if (level == null || level.isClientSide) {
            return;
        }

        applyLitProperty(emitLight);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);

        if (emitLight) {
            level.getChunkSource().getLightEngine().checkBlock(worldPosition);
        }
    }

    /** Keeps the block's "lit" blockstate in step with {@link #emitLight}. */
    private void applyLitProperty(boolean emitLight) {
        BlockState current = getBlockState();
        BooleanProperty lit = litProperty(current);
        if (lit != null && current.getValue(lit) != emitLight) {
            level.setBlock(worldPosition, current.setValue(lit, emitLight), Block.UPDATE_ALL);
        }
    }

    /**
     * Every block sharing this block entity declares its own "lit" property instance,
     * and properties compare by identity — so a static reference matches exactly one
     * of them and silently misses the rest. Look the property up off the state.
     */
    @Nullable
    private static BooleanProperty litProperty(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            if (property instanceof BooleanProperty bool && "lit".equals(bool.getName())) {
                return bool;
            }
        }
        return null;
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
        color = pTag.getInt("Color");
        emitLight = pTag.getBoolean("EmitLight");
        emitRedstone = pTag.getBoolean("EmitRedstone");
        passPlayer = pTag.getBoolean("PassPlayer");
        passEntity = pTag.getBoolean("PassEntity");
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
        if (tag.contains("Color")) {
            int oldColor = color;
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
