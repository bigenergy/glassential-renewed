package com.github.bigenergy.glassential.blocks.panes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DataComponentGetter;
import net.minecraft.world.level.block.IronBarsBlock;

import java.util.function.Consumer;

public class TintedBasicPaneBlock extends IronBarsBlock {

    private final boolean lightBlock;

    public TintedBasicPaneBlock(Properties properties, boolean lightBlock) {
        super(properties);
        this.lightBlock = lightBlock;
    }

    @Override
    public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
        if (lightBlock) {
            consumer.accept(Component.translatable("tooltip.glassential.light").withStyle(ChatFormatting.GRAY));
        }
    }
}
