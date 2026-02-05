package com.github.bigenergy.glassential.blocks;

import net.minecraft.world.level.block.TintedGlassBlock;

public class TintedLightGlassBlock extends TintedGlassBlock {

    public TintedLightGlassBlock(Properties settings) {
        super(settings.lightLevel((b) -> 15));
    }
}
