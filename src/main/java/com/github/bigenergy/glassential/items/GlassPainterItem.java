package com.github.bigenergy.glassential.items;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlassPainterItem extends Item {

    public GlassPainterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockEntity be = level.getBlockEntity(context.getClickedPos());

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

                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            openPainterScreen(stack);
        }

        return InteractionResult.SUCCESS;
    }

    @net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.CLIENT)
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
    }
}
