package com.github.bigenergy.glassential.network;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client -> server: sets the stored RGB of a Colorable Glass block entity.
 */
public class ColorUpdatePacket {

    private final BlockPos pos;
    private final int color;

    public ColorUpdatePacket(BlockPos pos, int color) {
        this.pos = pos;
        this.color = color;
    }

    public ColorUpdatePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.color = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.color);
    }

    public BlockPos pos() { return pos; }
    public int color() { return color; }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            BlockEntity be = player.level().getBlockEntity(this.pos);
            if (be instanceof ColorableGlassBlockEntity colorable) {
                colorable.setColor(this.color);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
