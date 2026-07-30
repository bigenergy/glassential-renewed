package com.github.bigenergy.glassential.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkGlassBlock extends AbstractGlassBlock {

	public DarkGlassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
		return world.getMaxLightLevel();
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);
		pTooltipComponents.add(Component.translatable("tooltip.glassential.dark").withStyle(ChatFormatting.GRAY));
	}
}
