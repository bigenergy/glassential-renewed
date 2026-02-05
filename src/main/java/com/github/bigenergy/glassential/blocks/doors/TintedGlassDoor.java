package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class TintedGlassDoor extends DoorBlock {
    public TintedGlassDoor(Properties properties, BlockSetType blockSetType) {
        super(blockSetType, properties.noOcclusion());
    }
}
