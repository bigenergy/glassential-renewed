package com.github.bigenergy.glassential.blocks.doors;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GlassDoor extends DoorBlock {
    public GlassDoor(Properties properties, BlockSetType blockSetType) {
        super(properties.noOcclusion(), blockSetType);
    }
}
