package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipGlassBlock extends AbstractGlassBlock {

    private final String tooltip;

    public TooltipGlassBlock(@NotNull Properties properties, @NotNull String tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @Nullable BlockGetter level,
            @NotNull List<Component> tooltipComponents,
            @NotNull TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable(this.tooltip).withStyle(ChatFormatting.GRAY));
    }
}
