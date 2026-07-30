package com.github.bigenergy.glassential.network;

import com.github.bigenergy.glassential.items.GlassPainterItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Client -> server: stores the brush settings on the held Glass Painter.
 *
 * <p>1.20.1 has no DataComponents, so the settings live in plain item NBT under the
 * same keys the 1.21 build uses ({@code Color}, {@code EmitLight}, {@code EmitRedstone},
 * {@code PassPlayer}, {@code PassEntity}).</p>
 */
public class GlassPainterPacket {

    private final int color;
    private final boolean emitLight;
    private final boolean emitRedstone;
    private final boolean passPlayer;
    private final boolean passEntity;

    public GlassPainterPacket(int color, boolean emitLight, boolean emitRedstone, boolean passPlayer, boolean passEntity) {
        this.color = color;
        this.emitLight = emitLight;
        this.emitRedstone = emitRedstone;
        this.passPlayer = passPlayer;
        this.passEntity = passEntity;
    }

    public GlassPainterPacket(FriendlyByteBuf buf) {
        this.color = buf.readInt();
        this.emitLight = buf.readBoolean();
        this.emitRedstone = buf.readBoolean();
        this.passPlayer = buf.readBoolean();
        this.passEntity = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.color);
        buf.writeBoolean(this.emitLight);
        buf.writeBoolean(this.emitRedstone);
        buf.writeBoolean(this.passPlayer);
        buf.writeBoolean(this.passEntity);
    }

    public int color() { return color; }
    public boolean emitLight() { return emitLight; }
    public boolean emitRedstone() { return emitRedstone; }
    public boolean passPlayer() { return passPlayer; }
    public boolean passEntity() { return passEntity; }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof GlassPainterItem)) {
                stack = player.getOffhandItem();
                if (!(stack.getItem() instanceof GlassPainterItem)) return;
            }

            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("Color", this.color);
            tag.putBoolean("EmitLight", this.emitLight);
            tag.putBoolean("EmitRedstone", this.emitRedstone);
            tag.putBoolean("PassPlayer", this.passPlayer);
            tag.putBoolean("PassEntity", this.passEntity);
        });
        ctx.get().setPacketHandled(true);
    }
}
