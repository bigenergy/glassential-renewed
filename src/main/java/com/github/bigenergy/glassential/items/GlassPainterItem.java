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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlassPainterItem extends Item {

    public GlassPainterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // Shift + right-click on any block = eyedropper: copy that block's color into the brush.
        if (player != null && player.isShiftKeyDown()) {
            if (level.isClientSide) {
                copyColorFromBlock(level, pos, context.getItemInHand(), player);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Regular right-click on a Colorable Glass: apply the brush's stored settings.
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ColorableGlassBlockEntity colorable) {
            ItemStack stack = context.getItemInHand();
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag tag = customData.copyTag();

            if (tag.contains("Color")) {
                int color = tag.getInt("Color");
                boolean emitLight = tag.getBoolean("EmitLight");
                boolean emitRedstone = tag.getBoolean("EmitRedstone");
                boolean passPlayer = tag.getBoolean("PassPlayer");
                boolean passEntity = tag.getBoolean("PassEntity");

                colorable.setColor(color);
                colorable.setEmitLight(emitLight);
                colorable.setEmitRedstone(emitRedstone);
                colorable.setPassPlayer(passPlayer);
                colorable.setPassEntity(passEntity);

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Determines the block's color and copies it into the brush.
     * Cascade:
     *   1. Our ColorableGlassBlockEntity → exact stored RGB.
     *   2. {@code BlockColors} tint handler (grass, leaves, water — vanilla + modded).
     *   3. {@code MapColor} fallback (works for virtually every block).
     */
    @OnlyIn(Dist.CLIENT)
    private void copyColorFromBlock(Level level, BlockPos pos, ItemStack stack, Player player) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        int color = -1;

        // 1. Our own Colorable Glass — read the exact RGB.
        if (be instanceof ColorableGlassBlockEntity colorable) {
            color = colorable.getColor() & 0xFFFFFF;
        }

        // 2. BlockColors tint (vanilla + any mod that registers handlers).
        if (color == -1) {
            int tint = Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
            if (tint != -1) {
                color = tint & 0xFFFFFF;
            }
        }

        // 3. MapColor fallback.
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

        // Preserve all other brush settings — only update the color.
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        boolean emitLight = tag.getBoolean("EmitLight");
        boolean emitRedstone = tag.getBoolean("EmitRedstone");
        boolean passPlayer = tag.getBoolean("PassPlayer");
        boolean passEntity = tag.getBoolean("PassEntity");

        PacketDistributor.sendToServer(
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
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Don't open the picker when sneaking — that combo is reserved for eyedropper / useOn.
        if (player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }

        if (level.isClientSide) {
            openPainterScreen(stack);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @OnlyIn(Dist.CLIENT)
    private void openPainterScreen(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        int currentColor = tag.getInt("Color");
        if (currentColor == 0) {
            currentColor = 0xFFFFFF;
        }
        boolean emitLight = tag.getBoolean("EmitLight");
        boolean emitRedstone = tag.getBoolean("EmitRedstone");
        boolean passPlayer = tag.getBoolean("PassPlayer");
        boolean passEntity = tag.getBoolean("PassEntity");

        Minecraft.getInstance().setScreen(
            new com.github.bigenergy.glassential.client.gui.GlassPainterScreen(
                stack, currentColor, emitLight, emitRedstone, passPlayer, passEntity
            )
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (tag.contains("Color")) {
            int color = tag.getInt("Color");
            String hexColor = String.format("#%06X", color);
            tooltipComponents.add(Component.translatable("tooltip.glassential.painter.color", hexColor).withStyle(ChatFormatting.GRAY));

            if (tag.getBoolean("EmitLight")) {
                tooltipComponents.add(Component.translatable("tooltip.glassential.painter.emit_light").withStyle(ChatFormatting.YELLOW));
            }
            if (tag.getBoolean("EmitRedstone")) {
                tooltipComponents.add(Component.translatable("tooltip.glassential.painter.emit_redstone").withStyle(ChatFormatting.RED));
            }
            if (tag.getBoolean("PassPlayer")) {
                tooltipComponents.add(Component.translatable("tooltip.glassential.painter.pass_player").withStyle(ChatFormatting.AQUA));
            }
            if (tag.getBoolean("PassEntity")) {
                tooltipComponents.add(Component.translatable("tooltip.glassential.painter.pass_entity").withStyle(ChatFormatting.GREEN));
            }
        } else {
            tooltipComponents.add(Component.translatable("tooltip.glassential.painter.usage").withStyle(ChatFormatting.GRAY));
        }

        tooltipComponents.add(Component.translatable("tooltip.glassential.painter.eyedropper").withStyle(ChatFormatting.DARK_GRAY));
    }
}
