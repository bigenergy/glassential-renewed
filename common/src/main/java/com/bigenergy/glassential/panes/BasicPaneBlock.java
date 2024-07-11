package com.bigenergy.glassential.panes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.IronBarsBlock;

import java.util.List;

public class BasicPaneBlock extends IronBarsBlock{

    private final boolean lightBlock;

    public BasicPaneBlock(Properties properties, boolean lightBlock) {
        super(properties);
        this.lightBlock = lightBlock;
    }


    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        pTooltipComponents.add(Component.translatable("tooltip.glassential.pane_frameless").withStyle(ChatFormatting.GRAY));

        if (this.lightBlock) {
            pTooltipComponents.add(Component.translatable("tooltip.glassential.pane_light").withStyle(ChatFormatting.GOLD));
        }
    }
}
