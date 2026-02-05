package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DataComponentGetter;
import net.minecraft.world.level.block.TransparentBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TooltipGlassBlock extends TransparentBlock {

    private final String tooltip;

    public TooltipGlassBlock(@NotNull Properties properties, @NotNull String tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        consumer.accept(Component.translatable(tooltip).withStyle(ChatFormatting.GRAY));
    }
}
