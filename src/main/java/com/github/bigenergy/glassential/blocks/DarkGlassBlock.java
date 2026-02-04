package com.github.bigenergy.glassential.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DarkGlassBlock extends TransparentBlock {

	public DarkGlassBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
		return 15; // Max light level
	}
}
