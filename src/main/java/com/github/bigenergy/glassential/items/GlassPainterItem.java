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
     * Client-side hook: opens the painter screen. Wired up in ClientModEvents.
     * On dedicated server this stays as a no-op.
     */
    public static Consumer<ItemStack> OPEN_SCREEN = stack -> {};

    /**
     * Client-side hook: runs the eyedropper color cascade and dispatches the packet.
     * Wired up in ClientModEvents. On dedicated server this stays as a no-op.
     * All references to client-only classes (Minecraft, BlockColors, ClientPacketDistributor)
     * live inside that lambda, so this class itself has no client classes baked into its bytecode.
     */
    public static Consumer<UseOnContext> EYEDROPPER = ctx -> {};

    public GlassPainterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        // Shift + right-click = eyedropper: copy that block's color into the brush.
        if (player != null && player.isShiftKeyDown()) {
            if (level.isClientSide()) {
                EYEDROPPER.accept(context);
            }
            return InteractionResult.SUCCESS;
        }

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
