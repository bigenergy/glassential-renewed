package com.github.bigenergy.glassential.blocks.panes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class DarkEtherealPaneBlock extends EtherealPaneBlock {

    public DarkEtherealPaneBlock(Properties properties, boolean collidePlayers) {
        super(properties, collidePlayers);
    }

    protected boolean propagatesSkylightDown(BlockState p_154824_, BlockGetter p_154825_, BlockPos p_154826_) {
        return false;
    }

    protected int getLightBlock(BlockState p_154828_, BlockGetter p_154829_, BlockPos p_154830_) {
        return 15; // Max light level
    }
}
