package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DataComponentGetter;

import java.util.function.Consumer;

public class DarkEtherealGlassBlock extends EtherealGlassBlock {

	public DarkEtherealGlassBlock(Properties properties, boolean collidePlayers) {
		super(properties, collidePlayers);
	}

	@Override
	public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
		super.addToTooltip(tooltipContext, consumer, tooltipFlag, dataComponentGetter);
		consumer.accept(Component.translatable("tooltip.glassential.dark").withStyle(ChatFormatting.GRAY));
	}
}
