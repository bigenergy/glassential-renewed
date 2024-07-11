package com.bigenergy.glassential.panes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DarkEtherealPaneBlock extends EtherealPaneBlock {


    public DarkEtherealPaneBlock(Properties properties, boolean collidePlayers) {
        super(properties, collidePlayers);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        pTooltipComponents.add(Component.translatable("tooltip.glassential.dark_pane").withStyle(ChatFormatting.GRAY));
    }
}
