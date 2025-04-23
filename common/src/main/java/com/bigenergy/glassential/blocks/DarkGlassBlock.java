package com.bigenergy.glassential.blocks;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DarkGlassBlock extends TransparentBlock implements TooltipProvider  {

	public DarkGlassBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public int getLightBlock(BlockState state) {
		return 15;
	}

	@Override
	public void addToTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) {
		consumer.accept(Component.translatable("tooltip.glassential.dark").withStyle(ChatFormatting.GRAY));
	}
}
