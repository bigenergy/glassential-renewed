package com.github.bigenergy.glassential.network;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ColorUpdatePacket(BlockPos pos, int color) implements CustomPacketPayload {

    public static final Type<ColorUpdatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("glassential", "color_update"));

    public static final StreamCodec<FriendlyByteBuf, ColorUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ColorUpdatePacket::pos,
            StreamCodec.of(
                    (buf, value) -> buf.writeInt(value),
                    FriendlyByteBuf::readInt
            ),
            ColorUpdatePacket::color,
            ColorUpdatePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ColorUpdatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                BlockEntity be = serverPlayer.level().getBlockEntity(packet.pos);
                if (be instanceof ColorableGlassBlockEntity colorable) {
                    colorable.setColor(packet.color);
                }
            }
        });
    }
}
