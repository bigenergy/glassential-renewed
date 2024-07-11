package com.bigenergy.glassential.blocks;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.TransparentBlock;

public class TooltipGlassBlock extends TransparentBlock {
	//Used for Light and Ghostly glass that don't need their own classes for their properties
	private String tooltip;

	public TooltipGlassBlock(Properties properties, String tooltip) {
		super(properties);
		this.tooltip = tooltip;
	}

	@Override
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
		super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
		pTooltipComponents.add(Component.translatable(this.tooltip).withStyle(ChatFormatting.GRAY));
	}

}
