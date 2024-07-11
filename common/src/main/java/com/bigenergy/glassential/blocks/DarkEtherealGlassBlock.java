package com.bigenergy.glassential.blocks;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DarkEtherealGlassBlock extends EtherealGlassBlock {

	public DarkEtherealGlassBlock(Block.Properties properties, boolean collidePlayers) {
		super(properties, collidePlayers);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
		return world.getMaxLightLevel();
	}

	@Override
	public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
		super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
		pTooltipComponents.add(Component.translatable("tooltip.glassential.dark").withStyle(ChatFormatting.GRAY));
	}
}
