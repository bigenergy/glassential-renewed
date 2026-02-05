package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DataComponentGetter;
import net.minecraft.world.level.block.TransparentBlock;

import java.util.function.Consumer;

public class LightGlassBlock extends TransparentBlock {

	public LightGlassBlock(Properties settings) {
		super(settings.lightLevel((b) -> 15));
	}

	@Override
	public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
		consumer.accept(Component.translatable("tooltip.glassential.light").withStyle(ChatFormatting.GRAY));
	}
}
