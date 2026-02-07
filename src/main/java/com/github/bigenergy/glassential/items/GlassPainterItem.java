package com.github.bigenergy.glassential.items;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class GlassPainterItem extends Item {

    public GlassPainterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockEntity be = level.getBlockEntity(context.getClickedPos());

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

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            openPainterScreen(stack);
        }

        return InteractionResult.SUCCESS;
    }

    private void openPainterScreen(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        int currentColor = tag.getIntOr("Color", 0xFFFFFF);
        if (currentColor == 0) {
            currentColor = 0xFFFFFF;
        }
        boolean emitLight = tag.getBooleanOr("EmitLight", false);
        boolean emitRedstone = tag.getBooleanOr("EmitRedstone", false);
        boolean passPlayer = tag.getBooleanOr("PassPlayer", false);
        boolean passEntity = tag.getBooleanOr("PassEntity", false);

        Minecraft.getInstance().setScreen(
            new com.github.bigenergy.glassential.client.gui.GlassPainterScreen(
                stack, currentColor, emitLight, emitRedstone, passPlayer, passEntity
            )
        );
    }
}
