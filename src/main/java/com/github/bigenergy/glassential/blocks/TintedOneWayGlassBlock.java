package com.github.bigenergy.glassential.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Tinted variant of OneWayGlassBlock — same one-way mimic behavior,
 * but blocks light like vanilla tinted glass.
 */
public class TintedOneWayGlassBlock extends OneWayGlassBlock {

    public TintedOneWayGlassBlock(Properties props) {
        super(props);
    }

    @Override
    protected int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return level.getMaxLightLevel();
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }
}
