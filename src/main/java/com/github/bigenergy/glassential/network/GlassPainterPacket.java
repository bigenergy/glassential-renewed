package com.github.bigenergy.glassential.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GlassPainterPacket(int color, boolean emitLight, boolean emitRedstone, boolean passPlayer, boolean passEntity) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<GlassPainterPacket> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("glassential", "glass_painter"));

    public static final StreamCodec<ByteBuf, GlassPainterPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.color);
                buf.writeBoolean(packet.emitLight);
                buf.writeBoolean(packet.emitRedstone);
                buf.writeBoolean(packet.passPlayer);
                buf.writeBoolean(packet.passEntity);
            },
            buf -> new GlassPainterPacket(
                    buf.readInt(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean()
            )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GlassPainterPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof com.github.bigenergy.glassential.items.GlassPainterItem) {
                CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                tag.putInt("Color", packet.color);
                tag.putBoolean("EmitLight", packet.emitLight);
                tag.putBoolean("EmitRedstone", packet.emitRedstone);
                tag.putBoolean("PassPlayer", packet.passPlayer);
                tag.putBoolean("PassEntity", packet.passEntity);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            } else {
                stack = player.getOffhandItem();
                if (stack.getItem() instanceof com.github.bigenergy.glassential.items.GlassPainterItem) {
                    CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                    tag.putInt("Color", packet.color);
                    tag.putBoolean("EmitLight", packet.emitLight);
                    tag.putBoolean("EmitRedstone", packet.emitRedstone);
                    tag.putBoolean("PassPlayer", packet.passPlayer);
                    tag.putBoolean("PassEntity", packet.passEntity);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
        });
    }
}
