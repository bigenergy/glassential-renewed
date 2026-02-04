package com.github.bigenergy.glassential.blocks.panes;

import net.minecraft.world.level.block.IronBarsBlock;

public class BasicPaneBlock extends IronBarsBlock{

    private final boolean lightBlock;

    public BasicPaneBlock(Properties properties, boolean lightBlock) {
        super(properties);
        this.lightBlock = lightBlock;
    }
}
