package com.bigenergy.glassential.blocks;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.TransparentBlock;

public class TooltipGlassBlock extends TransparentBlock implements TooltipProvider {
	//Used for Light and Ghostly glass that don't need their own classes for their properties
	private String tooltip;

	public TooltipGlassBlock(Properties properties, String tooltip) {
		super(properties);
		this.tooltip = tooltip;
	}

	@Override
	public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
		consumer.accept(Component.translatable(this.tooltip).withStyle(ChatFormatting.GRAY));
	}
}
