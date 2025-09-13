package com.github.bigenergy.glassential.blocks.entity;

import com.github.bigenergy.glassential.init.GlassentialBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

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

    public BlockState getMimic() { return mimic; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        var id = BuiltInRegistries.BLOCK.getKey(mimic.getBlock());
        if (id != null) tag.putString("Mimic", id.toString());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        BlockState def = Blocks.IRON_BLOCK.defaultBlockState();
        if (tag.contains("Mimic")) {
            var id = ResourceLocation.tryParse(tag.getString("Mimic"));
            if (id != null) {
                var opt = BuiltInRegistries.BLOCK.getOptional(id);
                if (opt.isPresent()) def = opt.get().defaultBlockState();
            }
        }
        mimic = def;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, lookup);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookup) {
        loadAdditional(tag, lookup);
        requestModelDataUpdate();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);  // шлём BE-пакет
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            loadAdditional(tag, lookup);
            requestModelDataUpdate();
            if (level != null && Minecraft.getInstance().levelRenderer != null) {
                Minecraft.getInstance().levelRenderer.blockChanged(
                        level,
                        worldPosition,
                        getBlockState(),                // old state
                        getBlockState(),                // new state
                        Block.UPDATE_CLIENTS
                );
            }
        }
    }
}
