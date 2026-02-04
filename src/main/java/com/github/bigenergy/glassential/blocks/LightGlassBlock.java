package com.github.bigenergy.glassential.blocks;

import net.minecraft.world.level.block.TransparentBlock;

public class LightGlassBlock extends TransparentBlock {

	public LightGlassBlock(Properties settings) {
		super(settings.lightLevel((b) -> 15));
	}
}
