package com.github.bigenergy.glassential.items;

import com.github.bigenergy.glassential.blocks.entity.ColorableGlassBlockEntity;
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
            OPEN_SCREEN.accept(stack);
        }

        return InteractionResult.SUCCESS;
    }
}
