package com.github.bigenergy.glassential.blocks.panes;

import net.minecraft.world.level.block.IronBarsBlock;

public class TintedBasicPaneBlock extends IronBarsBlock {

    private final boolean lightBlock;

    public TintedBasicPaneBlock(Properties properties, boolean lightBlock) {
        super(properties);
        this.lightBlock = lightBlock;
    }
}
