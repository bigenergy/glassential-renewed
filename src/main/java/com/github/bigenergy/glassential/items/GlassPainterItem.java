package com.github.bigenergy.glassential.items;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import com.github.bigenergy.glassential.network.GlassPainterPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GlassPainterItem extends Item {

    /**
     * Set from client-side initialization to open the painter screen.
     * On dedicated server this stays as no-op.
     */
    public static Consumer<ItemStack> OPEN_SCREEN = stack -> {};

    public GlassPainterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // Shift + right-click = eyedropper: copy that block's color into the brush.
        if (player != null && player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                copyColorFromBlock(level, pos, context.getItemInHand(), player);
            }
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof ColorableGlassBlockEntity colorable) {
            // Always consume the interaction on colorable glass to prevent
            // the GUI from opening (use() is called as fallback when useOn returns PASS)
            if (!level.isClientSide()) {
                ItemStack stack = context.getItemInHand();
                CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();

                int color = tag.getIntOr("Color", -1);
                if (color >= 0) {
                    colorable.setColor(color);
                    colorable.setEmitLight(tag.getBooleanOr("EmitLight", false));
                    colorable.setEmitRedstone(tag.getBooleanOr("EmitRedstone", false));
                    colorable.setPassPlayer(tag.getBooleanOr("PassPlayer", false));
                    colorable.setPassEntity(tag.getBooleanOr("PassEntity", false));
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    /**
     * Cascade: ColorableGlassBlockEntity → BlockColors tint → MapColor fallback.
     */
    @OnlyIn(Dist.CLIENT)
    private void copyColorFromBlock(Level level, BlockPos pos, ItemStack stack, Player player) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        int color = -1;

        if (be instanceof ColorableGlassBlockEntity colorable) {
            color = colorable.getColor() & 0xFFFFFF;
        }

        if (color == -1) {
            int tint = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
            if (tint != -1) {
                color = tint & 0xFFFFFF;
            }
        }

        if (color == -1) {
            MapColor mc = state.getMapColor(level, pos);
            if (mc != null && mc != MapColor.NONE) {
                color = mc.col & 0xFFFFFF;
            }
        }

        if (color == -1) {
            player.displayClientMessage(
                Component.translatable("message.glassential.brush.copy_failed")
                    .withStyle(ChatFormatting.RED),
                true
            );
            return;
        }

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        boolean emitLight = tag.getBooleanOr("EmitLight", false);
        boolean emitRedstone = tag.getBooleanOr("EmitRedstone", false);
        boolean passPlayer = tag.getBooleanOr("PassPlayer", false);
        boolean passEntity = tag.getBooleanOr("PassEntity", false);

        ClientPacketDistributor.sendToServer(
            new GlassPainterPacket(color, emitLight, emitRedstone, passPlayer, passEntity)
        );

        String hex = String.format("#%06X", color & 0xFFFFFF);
        player.displayClientMessage(
            Component.translatable("message.glassential.brush.copy_success", hex)
                .withStyle(ChatFormatting.GREEN),
            true
        );
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Don't open the picker when sneaking — reserved for eyedropper / useOn.
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide()) {
            OPEN_SCREEN.accept(stack);
        }

        return InteractionResult.SUCCESS;
    }
}
