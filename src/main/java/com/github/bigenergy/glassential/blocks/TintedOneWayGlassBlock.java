package com.github.bigenergy.glassential.blocks;

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
    protected int getLightDampening(BlockState state) {
        return 15;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return false;
    }
}
